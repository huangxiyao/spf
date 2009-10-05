#!perl
###############################################################
#ident "@(#) REL xlate_migrate.pl Release 3.0"
#ident "@(#) VER $Source: /external/Repo3/SP_FW/tools/admin_script/xlate_migrate.pl,v $ $Header: /external/Repo3/SP_FW/tools/admin_script/xlate_migrate.pl,v 1.9 2007/09/06 18:00:21 djorgen Exp $ $Locker:  $"
#ident "@(#) WHO Contact Marc DeRosa <derosa.marc@hp.com>"
#ident "@(#) CR (C) Copyright Hewlett-Packard Company, 2007"
#
# Description: xlate_migrate.pl : this perl script merges certain files from
# a vignette system car file into the component car file produced by the 
# service portal build process.
#
# Usage: xlate_migrate.pl dir1 dir2 
# 	dir1 - the directory containing the full portal car file as downloaded 
#              via the Vignette system admin download tool.  The script expects
#              this file to have its original name 'systemAdminSiteTruck.car'.
# 	dir2 - the directory containing the service portal built components 
#              car.  The script expects this file to have its original name 
#              'VAPComponents.car'
#       -help - help information
#
#
# History: 
#
# 1.0   05/05/07        MSD first version
# 2.0   Jul 2 07        DSJ second version
# 2.1   Aug 14 07       DSJ re-added .properties migration, clarified help
# 2.2   Aug 29 07       DSJ only copy files if they don't already exist
# 2.3   Sep 5 07        DSJ merge logo files too
# 3.0   Feb 16 09       DSJ update for Shared Portal Framework
# 3.1   Apr 17 09       DSJ update to migate layout config files too
#
################################################################
use strict;
use integer;
use Getopt::Long;
use File::Copy;
use Cwd;

#use Test::Simple tests => 19;
#if (isTest()) {
#    runAllTests();
#    exit 0;
#}

# Gather and validate command-line options.

my ($orgCarDir, $newCarDir) = &getCarDirs();

# Explode system and new CAR files.

print <<EOF;
---------------------------------------------------
----- Exploding CAR files.
---------------------------------------------------
EOF

print "----- Exploding reference CAR file in $orgCarDir\n";
if (!explodeCarsOnBasePath($orgCarDir)) {
    print "No CAR file found, exiting.\n";
    exit 0;
}

print "----- Exploding release CAR file in $newCarDir\n";
if (!explodeCarsOnBasePath($newCarDir)) {
    print "No CAR file found, exiting.\n";
    exit 0;
}

# Find files needing migration, and merge them into the new CAR.
# These are the files tagged _lc.* or _lc_CC.*, or named logo-*.jpg,
# or global help base files, or environment-specific layout configuration
# files in the original CAR, whose components still exist in the new CAR.
# DSJ 2007/09/05 1000460949
# DSJ 2009/4/17

print <<EOF;
---------------------------------------------------
----- Merging translated files, logo files, global help base files, and 
----- environment-specific layout configuration files from reference CAR
----- to release CAR.
---------------------------------------------------
EOF
my @localized_files = &findFiles(\&filterByLocalizableExtension, $orgCarDir);
my @logo_files = &findFiles(\&filterByLogoExtension, $orgCarDir);
my @globalhelp_files = &findFiles(\&filterByGlobalHelp, $orgCarDir);
my @layoutconfig_files = &findFiles(\&filterByLayoutConfig, $orgCarDir);

my $componentsRegex = vapComponentsRegex($newCarDir);

@localized_files = grep(/$componentsRegex/, @localized_files);
@logo_files = grep(/$componentsRegex/, @logo_files);
@globalhelp_files = grep(/$componentsRegex/, @globalhelp_files);
@layoutconfig_files = grep(/$componentsRegex/, @layoutconfig_files);

my $num_loc_files = @localized_files;
my $num_logo_files = @logo_files;
my $num_globalhelp_files = @globalhelp_files;
my $num_layoutconfig_files = @layoutconfig_files;

if ($num_loc_files + $num_logo_files + $num_globalhelp_files + 
    $num_layoutconfig_files == 0) {

    # Cleanup working files.

    chdir $orgCarDir or die "Error changing directory to $orgCarDir: $!\nDied";
    removeDirectories ("systemAdminSiteTruck");
    chdir $newCarDir or die "Error changing directory to $newCarDir: $!\nDied";
    removeDirectories ("VAPComponents");

    print "----- No files to merge.  You are free to import the original\n";
    print "VAPComponents.car file into Vignette.  Exiting.\n";
    exit 0;
}

print "----- Found $num_loc_files translated files, $num_logo_files logo files, $num_globalhelp_files global help\n";
print "base files, and $num_layoutconfig_files layout configuration files to merge.  Beginning merge.\n";
copyLocFilesToDest($orgCarDir, $newCarDir, @localized_files);
copyLogoFilesToDest($orgCarDir, $newCarDir, @logo_files);
copyGlobalHelpFilesToDest($orgCarDir, $newCarDir, @globalhelp_files);
copyLayoutConfigFilesToDest($orgCarDir, $newCarDir, @layoutconfig_files);

# Repack the merged components into a replacement CAR file.

print <<EOF;
---------------------------------------------------
----- Rebuilding release CAR file post-merge.
---------------------------------------------------
EOF
my $startDir = cwd();
reCarComponents($newCarDir . "/VAPComponents", 2);

# Finish remaining cleanup.

chdir $orgCarDir or die "Error changing directory to $orgCarDir: $!\nDied";
removeDirectories ("systemAdminSiteTruck");

# Done

print "----- Done.  The merged CAR file is in $newCarDir\n";
exit 0;

###############################################################
# Subroutines
###############################################################

sub getCarDirs() {
    my $opts_ref = readCommandLine();
    my %opts = %{$opts_ref};
    if (!validCommandOptions($opts_ref)) {
        exit 1;
    }
    my $orgCarDir = getCarDir ($opts{"orgCarDir"}, "systemAdminSiteTruck.car");
    my $newCarDir = getCarDir ($opts{"newCarDir"}, "VAPComponents.car");
    return ($orgCarDir, $newCarDir);
}

sub getCarDir() {
    my $carDir = shift;
    my $carFile = shift;

    # Make sure directory exists.
    unless (-d $carDir) {
        die "Directory $carDir does not exist.\nDied";
    }

    # Make sure directory contains only the expected CAR file.
    opendir(DIR_HDL, $carDir);
    my @files = readdir(DIR_HDL);
    closedir DIR_HDL;
    my $flag = 0;
    foreach (@files) {
        if (/^$carFile$/) {
            $flag = 1;
        } elsif (/^\.$|^\.\.$/) {
        } else {
            die "Directory $carDir must be empty except for $carFile\nDied";
        }
    }
    unless ($flag) {
        die "Directory $carDir is missing $carFile\nDied";
    }

    # Convert directory name to absolute path.
    my $cwd = cwd();
    chdir $carDir or die "Unable to change directory to $carDir: $!\nDied";
    $carDir = cwd();
    chdir $cwd or die "Unable to change directory back to $cwd: $!\nDied";
    return $carDir;
}

sub reCarComponents() {
    my $startDir = shift;
    my $depth = shift;
    chdir $startDir or die "Error changing directory to $startDir: $!\nDied";

    for (my $i = 0; $i < $depth; $i++) {
	my @dirs = findSubDirNames(\&filterByComponentName, ".");
	carDirectories(@dirs);
	removeDirectories(@dirs);
	chdir ".." or die "Error changing directory to ..: $!\nDied";
    }
}

sub carDirectories() {
    my $head = shift;
    my @tail = @_;
    if ($head eq undef) {
	return 0;
    } else {
	chdir "./$head" or die "Error changing directory to ./$head: $!\nDied";
        $head = "${head}.car";
        print "Creating CAR file ./$head\n";

        # jar in Cygwin does not like /cygdrive/c/... paths so convert
        # to c:/... paths.

        my $tfile = $head;
        $tfile =~ s?^/cygdrive/([a-z])/?$1:/?;
        system ("jar -cvf $tfile *");
        if ($?) {
            print "Error creating CAR file $head using jar: error code = $?\n";
            die;
        }
	rename ("./$head", "../$head") or die "Error moving ./$head to ../$head: $!\nDied";
	chdir ".." or die "Error changing directory to ..: $!\nDied";
	carDirectories(@tail);
    }
}

sub removeDirectories() {
    my $head = shift;
    my @tail = @_;
    if ($head eq undef) {
	return 0;
    } else {
        print "Removing directory tree $head\n";
        if (isMS()) {
            system("rmdir /s /q " . $head);
	    if ($?) {
	        print "Error deleting directory tree $head using rmdir /s /q: error code = $?\n";
                die;
            }
        } else {
	    system("rm -rf " . $head);
	    if ($?) {
	        print "Error deleting directory tree $head using rm -rf: error code = $?\n";
                die;
            }
	}
	removeDirectories(@tail);
    }
}

sub vapComponentsRegex() {
    my $newCarDir = shift;
    my $componentsDir = $newCarDir . "/VAPComponents";
    my $regex = "";
    my @dir_names = &findSubDirNames(\&filterByComponentName, $componentsDir);
    foreach (@dir_names) {
	$regex = $regex . $_ . "|";
    }
    return $regex . "Z_terminator_Z";
}

sub findSubDirNames() {
    my $filter_ref = shift;
    my $parentDir = shift;
    opendir(DIR_HDL, $parentDir);
    my @dir_names = readdir(DIR_HDL);
    closedir DIR_HDL;
    my @filtered_dirs = $filter_ref -> (@dir_names);
}

sub copyLocFilesToDest() {
    my $orgCarDir = shift;
    my $newCarDir = shift;
    my $head = shift;
    if ($head eq undef) {
	return;
    }

    my $head_destination = 
        sourceFileLocationToDestFileLocation($orgCarDir, $newCarDir, $head);
    my $head_destination_base = 
        locFileNameToNonLocFileName($head_destination);
    print "Copying $head to $head_destination\n";

    # Copy the translated file into the new location, if its base file exists
    # in the new location, and the translated file does not already exist in
    # the new location.  Checking for the base file ensures we don't migrate
    # translations of now-obsolete resource bundles.  Checking for the
    # translated file ensures we don't migrate anything that's already in the
    # new build.  DSJ 2007/08/29 1000448291

    if (fileExists($head_destination_base)) {
        if (!fileExists($head_destination)) {
            copy($head, $head_destination) 
               or die "Error copying $head to $head_destination: $!\nDied";
        }
        else {
            print "Translated file $head_destination already exists - skipping copy\n";
        }
    }
    else {
	print "Base file $head_destination_base does not exist - skipping copy\n";
    }

    return copyLocFilesToDest($orgCarDir, $newCarDir, @_);
}

# Need to copy logo files, too, into the new location.  Logo files are copied
# unless they already exist in the new location.  DSJ 2007/09/05 1000460949

sub copyLogoFilesToDest() {
    my $orgCarDir = shift;
    my $newCarDir = shift;
    my $head = shift;
    if ($head eq undef) {
	return;
    }

    my $head_destination = 
        sourceFileLocationToDestFileLocation($orgCarDir, $newCarDir, $head);
    print "Copying $head to $head_destination\n";
    if (!fileExists($head_destination)) {
        copy($head, $head_destination) 
            or die "Error copying $head to $head_destination: $!\nDied";
    }
    else {
        print "Logo file $head_destination already exists - skipping copy\n";
    }

    return copyLogoFilesToDest($orgCarDir, $newCarDir, @_);
}

# Need to copy global help files, too, into the new location.  The files are
# copied unless they already exist in the new location.  DSJ 2009/03/03

sub copyGlobalHelpFilesToDest() {
    my $orgCarDir = shift;
    my $newCarDir = shift;
    my $head = shift;
    if ($head eq undef) {
	return;
    }

    my $head_destination = 
        sourceFileLocationToDestFileLocation($orgCarDir, $newCarDir, $head);
    print "Copying $head to $head_destination\n";
    if (!fileExists($head_destination)) {
        copy($head, $head_destination) 
            or die "Error copying $head to $head_destination: $!\nDied";
    }
    else {
        print "Global help file $head_destination already exists - skipping copy\n";
    }

    return copyGlobalHelpFilesToDest($orgCarDir, $newCarDir, @_);
}

# Need to copy environment-specific layout configuration files, too, into the new
# location.  The files are copied unless they already exist in the new location.
# DSJ 2009/4/17

sub copyLayoutConfigFilesToDest() {
    my $orgCarDir = shift;
    my $newCarDir = shift;
    my $head = shift;
    if ($head eq undef) {
	return;
    }

    my $head_destination = 
        sourceFileLocationToDestFileLocation($orgCarDir, $newCarDir, $head);
    print "Copying $head to $head_destination\n";
    if (!fileExists($head_destination)) {
        copy($head, $head_destination) 
            or die "Error copying $head to $head_destination: $!\nDied";
    }
    else {
        print "Layout configuration file $head_destination already exists - skipping copy\n";
    }

    return copyLayoutConfigFilesToDest($orgCarDir, $newCarDir, @_);
}

sub fileExists() {
    my $file = shift;
    my $exists = -f $file;
    return $exists;
}

sub locFileNameToNonLocFileName() {
    my $fileName = shift;
    $fileName =~ s/_[a-z]{2}(\.[^\.]*$)|_[a-z]{2}_[a-z]{2}(\.[^\.]*$)/$1$2/i;
    return $fileName;
}

sub sourceFileLocationToDestFileLocation() {
    my $orgLocDir = shift;
    $orgLocDir = $orgLocDir . "/systemAdminSiteTruck/templatestyle";
    my $newLocDir = shift;
    $newLocDir = $newLocDir . "/VAPComponents";
    my $orgPath = shift;
    $orgPath =~ s/$orgLocDir/$newLocDir/;
    return $orgPath;
}

sub findFiles() {
    my $file_filter_ref = shift;
    my $firstBaseDir = shift;

    opendir(DIR_HDL, $firstBaseDir);
    my @content = readdir(DIR_HDL);
    closedir DIR_HDL;
    my @content_files = $file_filter_ref->($firstBaseDir, @content);
    my @content_dirs = filterDirs($firstBaseDir, @content);
    push(@content_dirs, @_);
    if (0 <  @content_dirs) {
	push(@content_files, findFiles($file_filter_ref, @content_dirs));
    }
    return @content_files;
}

sub filterByComponentName() {
    my @directories = @_;
    return grep(!/\.|META-INF/, @directories);
}

sub filterByCarExtension() {
    my $baseDir = shift;
    my @content = @_;
    my @content_cars = grep(/\.car$/, @content); 
    return map {$baseDir . "/" . $_} @content_cars;
}

sub filterByLocalizableExtension() {
    my $baseDir = shift;
    my @content = @_;
    my @content_loc = grep(/_[a-z]{2}\.[^\.]*$|_[a-z]{2}_[a-z]{2}\.[^\.]*$/i, 
                           @content);

# Earliest versions of this code migrated all translation files regardless of
# type.  Later versions excluded .properties files from that migration, since
# it seemed they did not need migration (since it seemed Vignette wouldn't
# erase them upon deployment anyway).  But now we have found cases (root
# cause unknown) where Vignette is erasing .properties files after all.  To
# workaround that, I am returning this code to the earliest behavior, so even
# translated .properties files will now be migrated too.  DSJ 2007/08/14
#
#   my @content_loc_no_property_files = grep(!/\.properties$/, @content_loc);
#   return map {$baseDir . "/" . $_} @content_loc_no_property_files;

    return map {$baseDir . "/" . $_} @content_loc;
}

# Need to migrate logo files, too.  Logo files are those named logo-*.jpg.
# This is because logo files are created and uploaded to Vignette outside of
# the SP release/build process, like translation files.  So they must be 
# migrated in the same way.  DSJ 2007/09/05 1000460949

sub filterByLogoExtension() {
    my $baseDir = shift;
    my @content = @_;
    my @content_logos = grep(/^logo\-.*\.jpg$/, @content);
    return map {$baseDir . "/" . $_} @content_logos;
}

# Need to migrate global help base files, too.  These are all files ending in
# "[gG]lobalHelp.html", "[gG]lobalHelpInclude.properties", or
# "GlobalHelpResource.*".

sub filterByGlobalHelp() {
    my $baseDir = shift;
    my @content = @_;
    my @content_globalhelp_basefiles = grep (
       /[gG]lobalHelp\.html$|[gG]lobalHelp\.css$|[gG]lobalHelp.js$|[gG]lobalHelpInclude\.properties$|GlobalHelpResource\.[^\.]*$/,
       @content);
    return map {$baseDir . "/" . $_} @content_globalhelp_basefiles;
}

# Need to migrate environment-specific layout configuration files, too.
# These are all files ending in "_layout_config_env.*".

sub filterByLayoutConfig() {
    my $baseDir = shift;
    my @content = @_;
    my @content_layoutconfig_files = grep (
       /_layout_config_env\.[^\.]*$/,
       @content);
    return map {$baseDir . "/" . $_} @content_layoutconfig_files;
}

sub filterDirs() {
    my $baseDir = shift;
    my @content = @_;
    my @content_dirs = grep(!/\.[a..bA..Z]*/, @content);
    return map {$baseDir . "/" . $_} @content_dirs;
}

sub readCommandLine() {
    my $preview = 0; 
    my $help = 0;
    GetOptions ("help" => \$help);
    my ($orgCarDir, $newCarDir) = readSourceAndDestDirs(@ARGV);
    $orgCarDir =~ s/\/$//;
    $newCarDir =~ s/\/$//;
    my %result = (help => $help,
                  orgCarDir => $orgCarDir, 
                  newCarDir => $newCarDir);
    return \%result;
}

sub readSourceAndDestDirs() {
    my @argv = @_;
    my $regex = "-help|--help";
    return grep(!/$regex/, @argv);
}

sub validCommandOptions() {
    my $opts_ref = shift;
    my %opts = %{$opts_ref};
    if ($opts{"help"} == 1) {
	printHelpMessage();
	return 0;
    }

    my $isValid = 1;
    if ($opts{"orgCarDir"} eq "" || $opts{"newCarDir"} eq "") {
	print "Both truckDir and buildDir must be specified on the command line.\n";
        printHelpMessage();
	$isValid = 0;
    }
    return $isValid;
}

sub printHelpMessage() {
    print <<EOF;
usage: xlate_migrate.pl truckDir buildDir
truckDir = The directory containing the CAR file downloaded from Vignette. 
           The CAR file must be named systemAdminSiteTruck.car
buildDir = The directory containing the new CAR file generated by the Service
           Portal build.  The CAR file must be named VAPComponents.car
EOF
}

sub explodeCarsOnBasePath() {
    my $baseDir = shift;
    my @cars = &findFiles(\&filterByCarExtension, $baseDir);
    if (0 == @cars) {
	return 0;
    } else {
	extractCars(@cars);
	deleteCars(@cars);
	return explodeCarsOnBasePath($baseDir) || 1;
    }
}

sub deleteCars() {
    my $head = shift;
    my @tail = @_;
    if ($head eq undef) {
	return 0;
    } else {
        print "Removing CAR file $head\n";
	unless (unlink $head) {
	    print "Error deleting CAR file " . $head . ", error = " . $! . "\n";
	    die;
	}
	deleteCars(@tail);
    }
}

sub extractCars() {
    my $head = shift;
    my @tail = @_;
    if ($head eq undef) {
	return 0;
    } else {
        my $dir = getExpansionDirFromCarPath($head);
        my $cwd = cwd();

        print "Extracting CAR file $head to $dir\n";

        # jar requires us to be in the directory, so cd there and then back.
        # also, jar in Cygwin does not like /cygdrive/c/... paths so convert
        # to c:/... paths.

        mkdir $dir;
        chdir $dir or die "Unable to change directory to $dir: $!\nDied";
        my $tfile = $head;
        $tfile =~ s?^/cygdrive/([a-z])/?$1:/?;
        system ("jar -xvf $tfile");
        if ($?) {
            print "Error extracting CAR file $head using jar: error code = $?\n";
            die;
        }
        chdir $cwd or die "Unable to change directory back to $cwd: $!\nDied";

	extractCars(@tail);
    }
}

sub getExpansionDirFromCarPath() {
    my $path = shift;
    my @path_list = split(/\/|\./, $path);
    my $size = @path_list;
    my $new_dir = $path_list[$size-2];
    my $base_dir = substr($path, 0, index($path, $new_dir . ".car"));
    return $base_dir . $new_dir;
}

sub isMS {
    # Try DOS VER command to discover if this is a Microsoft platform
    my $ms = system ("ver") ? 0 : 1;
    return $ms;
}

sub isTest() {
    return ($ARGV[$#ARGV] eq "--test");
}

# This test driver subroutine is obsolete and needs to be rewritten to work.
# 2007/09/05 DSJ 1000460949
# 
# sub runAllTests() {
#     my $actual_s;
#     my @actual_l;
#     my @data_l;
#     @data_l = ("file1.car", "file2.car", ".", "..", "dir1", "dir2", "file1.xml", "file1.img");
#     @actual_l = &filterByCarExtension("./base", @data_l);
#     ok (2 == @actual_l, "expected a list of size 2 found list of size " . @actual_l);
#     ok ("./base/file1.car" eq $actual_l[0], "expected './base/file1.car' found " . $actual_l[0] . "'");
#     @actual_l = &filterDirs("./base", @data_l);
#     ok (2 == @actual_l, "expected a list of size 2 found list of size " . @actual_l);
#     ok ("./base/dir1" eq $actual_l[0], "expected './base/dir1' found '" . $actual_l[0] . "'");
#     @data_l = ("./old", "./new");
#     @actual_l = &readSourceAndDestDirs(@data_l);
#     ok ("./old" eq $actual_l[0], "expected './old' found '" . @actual_l[0] . "'");
# 
# 
#     ok ("./new" eq $actual_l[1], "expected './new' found '" . @actual_l[1] . "'");
#     @data_l = ("./old", "./new");
#     @actual_l = &readSourceAndDestDirs(@data_l);
#     ok ("./old" eq $actual_l[0], "expected './old' found '" . @actual_l[0] . "'");
#     ok ("./new" eq $actual_l[1], "expected './new' found '" . @actual_l[1] . "'");
#     @data_l = ("./dest/dir", "./old");
#     @actual_l = &readSourceAndDestDirs("./dest/dir", @data_l);
#     ok (1 eq @actual_l, "new car file not specified expected list of size 1");
#     $actual_s = &getExpansionDirFromCarPath("./baseDir/output/stuff.car");
#     ok ("./baseDir/output/stuff" eq $actual_s, "function found dir '" . $actual_s . "'");
# 
# 
#     @data_l = ("./old", "./new");
#     @actual_l = &readSourceAndDestDirs("", @data_l);
#     ok (2 == @actual_l, "list has total of " . @actual_l . " elements");
#     @data_l = ("SiteManager.systemAdminSite.car");
#     @actual_l = &filterByCarExtension(".", @data_l);
#     ok (1 == @actual_l, "found " . @actual_l . " elements in list, expected 1");
#     @actual_l = &filterByLocalizableExtension("./basedir", 
# 					     ("file.txt", "file.properties", "file.html",
# 					      "file_en.properties", "file_zh_CH.properties"));
#     ok (2 == @actual_l, "found " . @actual_l . " elements in list expected 2");
#     ok ($actual_l[0] eq "./basedir/file_en.properties", "found '" . $actual_l[0] . "'");
#     $actual_s = &locFileLocationToDestFileLocation("./old", "./new", 
# 						  "./old/systemAdminSiteTruck/templatestyle/" .
# 						  "ServicePortalAccessErrorPage/WEB-INF/i18n/" .
# 						  "ServicePortalAuthErrorPage_en.properties");
#     ok ("./new/VAPComponents/ServicePortalAccessErrorPage/WEB-INF/i18n/ServicePortalAuthErrorPage_en.properties" eq $actual_s, "return path = '" . $actual_s . "'");
# 
# 
#     $actual_s = &locFileNameToNonLocFileName("./some/dir/localized_fr.html");
#     ok ("./some/dir/localized.html" eq $actual_s, "base file name is '" . $actual_s . "'");
#     $actual_s = &locFileNameToNonLocFileName("adir/web-inf/localized_en_CA.properties");
#     ok ("adir/web-inf/localized.properties" eq $actual_s, "base file name is '" . $actual_s . "'");
#     $actual_s = &fileExists("./yo/this/doesnt/exist");
#     ok (0 == $actual_s, "expected 0 -- file should not exist");
#     @actual_l = &filterByComponentName(".","..", "META-INF", "component");
#     ok (1 == @actual_l, "expected a list of length 1");
# }

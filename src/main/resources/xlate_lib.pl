###############################################################
#ident "@(#) REL xlate_lib.pl Release 3.0"
#ident "@(#) VER $Source: /external/Repo3/SP_FW/tools/admin_script/xlate_lib.pl,v $ $Header: /external/Repo3/SP_FW/tools/admin_script/xlate_lib.pl,v 1.17 2007/12/13 19:12:16 djorgen Exp $ $Locker:  $"
#ident "@(#) WHO Contact Scott Jorgenson <scott.jorgenson@hp.com>"
#ident "@(#) CR (C) Copyright Hewlett-Packard Company, 2009"
#
# Description: xlate_lib.pl : a perl library, used by the SPF translation
#              scripts
#
# History: 
#
# 2.0   06/19/07        DSJ created
# 2.1   12/13/07        DSJ updated for QXCR1000756101
# 2.2   1/2/08          DSJ updated again for QXCR1000756101
# 3.0   02/16/09        DSJ updated for Shared Portal Framework
# 3.1   04/01/09        DSJ updated for Hebrew
#
################################################################
package main;

use Cwd;
use File::Temp qw/ tempfile tempdir /;
use integer;

# RegExpressions for matching text and media files

$msg_ext_re = "properties";
$nonmsg_ext_re = "html|htm|xml|txt|css|js|rtf";
$image_ext_re = "jpg|gif|tif";
$misc_ext_re = "pdf|doc|mpg|wav|psd|wmv|avi|ppt";
$media_ext_re = "$image_ext_re|$misc_ext_re";
$nonnormal_tags_re = "es_es|fr_ca|pt_br|zh_tw|zh_hk";

$all_msg_text_files_re = "^.*\.($msg_ext_re)\$";
$all_nonmsg_text_files_re = "^.*\.($nonmsg_ext_re)\$";
$all_text_files_re = "^.*\.($msg_ext_re|$nonmsg_ext_re)\$";
$all_image_files_re = "^.*\.($image_ext_re)\$";
$all_misc_files_re = "^.*\.($misc_ext_re)\$";
$all_media_files_re = "^.*\.($media_ext_re)\$";
$all_vgn_portal_msg_files_re = "^[a-f0-9]{32}.*\.($msg_ext_re)\$";
$all_spf_portal_msg_files_re = "^spf\-.*\.($msg_ext_re)\$";
# PROJECT will be replaced with the project name at run time
$all_project_portal_msg_files_re = "^PROJECT.*\.($msg_ext_re)\$";
$all_xlated_files_re = "_[a-zA-Z]{2}\.[^\.]*\$";
$all_xlated_text_files_re = "^.*_[a-zA-Z]{2}\.($msg_ext_re|$nonmsg_ext_re)\$";
$all_xlated_media_files_re = "^.*_[a-zA-Z]{2}\.($media_ext_re)\$";
$all_nonnormal_xlated_files_re = "_($nonnormal_tags_re)\.[^\.]*\$";
@all_multicode_locale_res = ("he|iw", "yi|ji", "id|in"); 

@no_xlate_msg_keys = ("description", "[^\=]*_comment");

#
# Subroutine get_project
#

sub get_project  {

   local ($project_opt) = @_;
   local ($project);

   $project = $project_opt;
   $project =~ s/^[\s\/]*//g;     # Trim any leading whitespace or dir chars
   $project =~ s/[\s\/]*$//g;     # Trim any trailing whitespace or dir chars

   unless (&check_opt_value($project))  {
      print "Invalid project name. Project name may not contain these characters: . _ / \\\n";
      &print_usage ();
      exit 1;
   }
   # Specify the projecxt name in the global variable
   $all_project_portal_msg_files_re =~ s/PROJECT/$project/g;
   return $project;
}

#
# Subroutine get_cycle
#

sub get_cycle  {

   local ($cycle_opt) = @_;
   local ($cycle);

   $cycle = $cycle_opt;
   $cycle =~ s/^[\s\/]*//g;     # Trim any leading whitespace or dir chars
   $cycle =~ s/[\s\/]*$//g;     # Trim any trailing whitespace or dir chars

   unless (&check_opt_value($cycle))  {
      print "Invalid cycle name. Cycle name may not contain these characters: . _ / \\\n";
      &print_usage ();
      exit 1;
   }
   return $cycle;

}

#
# Subroutine check_opt_value
#

sub check_opt_value  {
   local ($value) = @_;
   if (($value =~ /\//) || ($value =~ /\\/) || 
       ($value =~ /\./) || ($value =~ /\_/))  {
      return 0;
   }
   else {
      return 1;
   }
}

#
# Subroutine get_project_dir
#

sub get_project_dir  {

   local ($base_dir, $project) = @_;
   local ($project_dir);

   $project_dir = "$base_dir/$project";
   return $project_dir;

}

#
# Subroutine get_cycle_dir
#

sub get_cycle_dir  {

   local ($base_dir, $project, $cycle) = @_;
   local ($cycle_dir);

   $cycle_dir = "$base_dir/$project/$cycle";
   return $cycle_dir;

}
   
#
# Subroutine check_dirs
#

sub check_dirs  {

   local ($base_dir, $project, $cycle, @check_dirs) = @_;
   local ($check_dir);

   if (!-d $base_dir)  {
      print "Base directory $base_dir does not exist.\n";
      die;
   }

   local ($project_dir) = &get_project_dir ($base_dir, $project);
   if (!-d $project_dir)  {
      print "Project directory $project_dir does not exist.\n";
      print "Run:\n  xlate_setup.pl --p $project --c $cycle --d $base_dir\nthen try again.\n";
      die;
   }

   local ($cycle_dir) = &get_cycle_dir ($base_dir, $project, $cycle);
   if (!-d $cycle_dir)  {
      print "Cycle directory $cycle_dir does not exist.\n";
      print "Run:\n  xlate_setup.pl --p $project --c $cycle --d $base_dir\nthen try again.\n";
      die;
   }

   while ($check_dir = shift @check_dirs)  {
      if (!-d $check_dir)  {
         print "Required directory $check_dir does not exist.\n";
         print "Run:\n  xlate_setup.pl --p $project --c $cycle --d $base_dir\nthen try again.\n";
         die;
      }
   }
   return;

}

#
# Subroutine tar_files
#
# Tars all files in the from_dir matching the given regular expression 
# (default: all files) to the tar_file.
#

sub tar_files  {

   local ($from_dir, $tar_file, $tar_these_files_re) = @_;
   local ($file, $errc, $cwd);
   local @files = ();
   $tar_these_files_re = $tar_these_files_re || ".*";

   unless (-d $from_dir) {
      print "Required directory $from_dir does not exist.\n";
      die;
   }

   # Get list of files to TAR.

   opendir FILES, $from_dir or die "Cannot open directory $from_dir: $!\nDied";
   while (defined ($file = readdir (FILES)))  {
      if ((-f "$from_dir/$file") && ($file =~ /$tar_these_files_re/i)) {
         push @files, $file;
      }
   }
   closedir FILES or die "Unable to close directory $from_dir: $!\n\nDied";

   # TAR must be done in the from_dir as current working dir.
   # Remember what the cwd was, and switch back to it afterwards.

   $cwd = cwd();
   chdir $from_dir or die "Unable to change directory to $from_dir: $!\nDied";

   # Now do the TAR (unless no files).
   # Before starting, delete any existing tar file of the same name.

   print "Tarring $from_dir to $tar_file\n";
   if (-f $tar_file)  {
      print "Removing existing $tar_file\n";
      unlink ($tar_file) or die "Cannot remove previous file $tar_file: $!\nDied";
   }
   if (! @files)  {
      print "No files to pack.  No need to create $tar_file\n";
      return;
   }

   if (&is_ms()) {  # Microsoft: assume 7-Zip 7ZA is installed

# Replaced PowerArchiver (fee) with 7-Zip (free) - DSJ 2007/07/13
#     $errc = system ("pacomp", "-a", $tar_file, @files);
#     if ($errc)  {
#        die "PowerArchiverCL PACOMP of $from_dir to $tar_file failed with error code " . $errc/256 . "\nDied";
#     }

      # For QXCR1000756101: Now add to tar file only in increments of 100 files,
      # to be sure this scales.  7ZIP "a" option adds to an existing archive,
      # or creates it if it does not already exist.  DSJ 2007/12/13
      while (scalar @files) {
         @some_files = splice (@files, 0, 100);
         $errc = system ("7za", "a", "-ttar", $tar_file, @some_files);
         if ($errc)  {
            die "7-Zip 7ZA add of $from_dir to $tar_file failed with error code " . $errc/256 . "\nDied";
         }
      }
   }
   else {  # Else default (UX): assume tar is installed
      # For QXCR1000756101: Now add to tar file only in increments of 100 files,
      # to be sure this scales.  Tar "u" option adds to an existing archive;
      # "c" option creates it if it does not already exist.  DSJ 2007/12/13,
      # 2008/1/2
      while (scalar @files) {
         @some_files = splice (@files, 0, 100);
         $option = -f $tar_file ? "u" : "c";
         $errc = system ("tar", "-${option}vf", $tar_file, @some_files);
         if ($errc)  {
            die "Tar of $from_dir to $tar_file failed with error code " . $errc/256 . "\nDied";
         }
      }
   }
   print "$tar_file is ready.\n";
   chdir $cwd or die "Unable to change directory back to $cwd: $!\nDied";
   return;

}

#
# Subroutine zip_files
#
# Zips all files in the from_dir matching the given regular expression 
# (default: all files) to the zip_file.
#

sub zip_files  {

   local ($from_dir, $zip_file, $zip_these_files_re) = @_;
   local ($file, $errc, $cwd, $tfile);
   local @files = ();
   $zip_these_files_re = $zip_these_files_re || ".*";

   unless (-d $from_dir) {
      print "Required directory $from_dir does not exist.\n";
      die;
   }

   # Get list of files to zip.

   opendir FILES, $from_dir or die "Cannot open directory $from_dir: $!\nDied";
   while (defined ($file = readdir (FILES)))  {
      if ((-f "$from_dir/$file") && ($file =~ /$zip_these_files_re/i)) {
         push @files, $file;
      }
   }
   closedir FILES or die "Unable to close directory $from_dir: $!\nDied";

   # Zip must be done in the from_dir as current working dir.
   # Remember what the cwd was, and switch back to it afterwards.

   $cwd = cwd();
   chdir $from_dir or die "Unable to change directory to $from_dir: $!\nDied";

   # Now do the zip (unless no files).
   # Before starting, delete any existing zip file of the same name.

   print "Zipping $from_dir to $zip_file\n";
   if (-f $zip_file)  {
      print "Removing existing $zip_file\n";
      unlink ($zip_file) or die "Cannot remove previous file $zip_file: $!\nDied";
   }
   if (! @files)  {
      print "No files to pack.  No need to create $zip_file\n";
      return;
   }

   if (&is_ms()) {  # Microsoft: assume 7-Zip 7ZA is installed
# Replaced PowerArchiver (fee) with 7-Zip (free) - DSJ 2007/07/13
#     $errc = system ("pacomp", "-a", $zip_file, @files);
#     if ($errc)  {
#        die "PowerArchiverCL PACOMP of $from_dir to $zip_file failed with error code " . $errc/256 . "\nDied";
#     }

      # For QXCR1000756101: Now add to zip file only in increments of 100 files,
      # to be sure this scales.  7ZIP "a" option adds to an existing archive,
      # or creates it if it does not already exist.  DSJ 2007/12/13
      while (scalar @files) {
         @some_files = splice (@files, 0, 100);
         $errc = system ("7za", "a", "-tzip", $zip_file, @some_files);
         if ($errc)  {
            die "7-Zip 7ZA add of $from_dir to $zip_file failed with error code " . $errc/256 . "\nDied";
         }
      }
   }
   else {  # Else default (UX): assume jar is installed

# Replaced Info-Zip unzip (non-standard) with jar (standard) - DSJ 2007/07/13
#     $errc = system ("zip", "-j", $zip_file, @files);
#     if ($errc)  {
#        die "Zip of $from_dir to $zip_file failed with error code " . $errc/256 . "\nDied";
#     }

      # In Cygwin environment, jar does not like /cygdrive/c/... path - so 
      # convert to c:/... path.
      $tfile = $zip_file;
      $tfile =~ s?^/cygdrive/([a-z])/?$1:/?;

      # For QXCR1000756101: Now add to ZIP file only in increments of 100 files,
      # to be sure this scales.  Jar "c" option creates a new archive always,
      # while "a" option only adds to an existing archive and throws an 
      # exception if it does not exist; so therefore we must switch between the
      # proper option.  DSJ 2007/12/13
      while (scalar @files) {
         @some_files = splice (@files, 0, 100);
         $switch = -f $zip_file ? "u" : "c";
         $errc = system ("jar", "-${switch}Mvf", $tfile, @some_files);
         if ($errc)  {
            die "Jar of $from_dir to $zip_file failed with error code " . $errc/256 . "\nDied";
         }
      }
   }
   print "$zip_file is ready.\n";
   chdir $cwd or die "Unable to change directory back to $cwd: $!\nDied";
   return;

}

#
# Subroutine unpack_files
#

sub unpack_files  {

   local ($from_dir, $to_dir) = @_;
   local ($file, $errc, $cwd);
   local ($unpack) = 0;

   opendir PACKS, $from_dir or die "Cannot open directory $from_dir: $!\nDied";
   while (defined ($file = readdir (PACKS)))  {
      $file = "$from_dir/$file";
      if ( -f $file )  {

         # Unpack ZIP and JAR files.

         if ($file =~ /\.(zip|jar)$/i)  {
            print "Unzipping $file to $to_dir\n";
            if (&is_ms()) {  # Microsoft: assume 7-Zip 7ZA installed
# Replaced PowerArchiver (fee) with 7-Zip (free) - DSJ 2007/07/13
#              $errc = system ("paext", "-e", "-o+", "-p\"$to_dir\"", $file);
#              if ($errc)  {
#                 die "PowerArchiverCL PAEXT of $file to $to_dir failed with error code " . $errc/256 . "\nDied";
#              }
               $errc = system ("7za", "e", "-y", "-o\"$to_dir\"", $file);
               if ($errc)  {
                  die "7-Zip 7ZA extract of $file to $to_dir failed with error code " . $errc/256 . "\nDied";
               }
            }
            else {      # Else default (UX): assume jar is installed

# Replaced Info-Zip unzip (non-standard) with jar (standard) - DSJ 2007/07/13
#              $errc = system ("unzip", "-o", $file, "-d", $to_dir);
#              if ($errc)  {
#                 die "Unzip of $file to $to_dir failed with error code " . $errc/256 . "\nDied";
#              }

               # jar needs to operate in the target directory, so temporarily
               # cd to that directory.  Also, in Cygwin environment, jar does
               # not like /cygdrive/c/... path - so convert to c:/... path.

               $cwd = cwd();
               chdir $to_dir or die "Unable to change directory to $to_dir: $!\nDied";
               $tfile = $file;
               $tfile =~ s?^/cygdrive/([a-z])/?$1:/?;
               $errc = system ("jar", "-xvf", $tfile);
               if ($errc)  {
                  die "Unjar of $file to $to_dir failed with error code " . $errc/256 . "\nDied";
               }
               chdir $cwd or die "Unable to change directory back to $cwd: $!\nDied";
            }
            $unpacked++;
         }

         # Unpack TAR files.

         elsif ($file =~ /\.tar$/i)  {
            print "Untarring $file to $to_dir\n";
            if (&is_ms()) {  # Microsoft: assume 7-Zip 7ZA installed
# Replaced PowerArchiver (fee) with 7-Zip (free) - DSJ 2007/07/13
#              $errc = system ("paext", "-e", "-o+", "-p\"$to_dir\"", $file);
#              if ($errc)  {
#                 die "PowerArchiverCL PAEXT of $file to $to_dir failed with error code " . $errc/256 . "\nDied";
#              }
               $errc = system ("7za", "e", "-y", "-o\"$to_dir\"", $file);
               if ($errc)  {
                  die "7-Zip 7ZA extract of $file to $to_dir failed with error code " . $errc/256 . "\nDied";
               }
            }
            else  {          # Else default (UX): assume tar is installed

               # tar needs to operate in the target directory, so temporarily
               # cd to that directory. 

               $cwd = cwd();
               chdir $to_dir or die "Unable to change directory to $to_dir: $!\nDied";
               $errc = system ("tar", "-xvf", $file);
               if ($errc)  {
                  die "Untar of $file to $to_dir failed with error code " . $errc/256 . "\nDied";
               }
               chdir $cwd or die "Unable to change directory back to $cwd: $!\nDied";
            }
            $unpacked++;
         }
      }
   }
   closedir PACKS or die "Cannot close directory $from_dir: $!\nDied";
   unless ($unpacked)  {
      print "No packages to unpack in $from_dir\n";
   }
   return;

}

# 
# Subroutine copy_files
#

sub copy_files  {

   local ($from_dir, $to_dir, $regexp) = @_;
   local ($file, $filename);
   local ($copied) = 0;

   opendir FILES, $from_dir or die "Cannot open directory $from_dir: $!\nDied";
   while (defined ($filename = readdir (FILES)))  {
      $file = "$from_dir/$filename";
      if ( -f $file && $filename =~ /$regexp/i )  {
         print "Copying $file to $to_dir\n";
         copy ($file, $to_dir) or die "Cannot copy $file to $to_dir: $!\nDied"; 
         $copied++;
      }
   }
   closedir FILES or die "Cannot close directory $from_dir: $!\nDied";
   unless ($copied)  {
      print "No files copied from $from_dir\n";
   }
   return;

}

# 
# Subroutine convert_files
#
# This sub takes directory in which to operate and regexp for files upon which
# to operate.  Every file in the directory matching the pattern will be
# operated upon by the native2ascii Java utility into a temporary file which
# will then be renamed back on top of the original.


sub convert_files  {

   local ($dir, $re) = @_;
   local ($file, $tfile, $filex, $tfilex, $filename, $errc);
   local ($converted) = 0;

   # Search for files in the dir matching the regexp and run native2ascii
   # against them, putting the output into a tempfile.  When done, rename the
   # tempfile back on top of the original.  We assume the file to convert is 
   # encoded with UTF-8.

   opendir FILES, $dir or die "Cannot open directory $dir: $!\nDied";
   while (defined ($filename = readdir (FILES)))  {
      $file = "$dir/$filename";
      if ( -f $file && $filename =~ /$re/i )  {
         (undef, $tfile) = tempfile ("$file.XXXXXX", OPEN => 0);
         print "Converting $file\n";
      
         # In Cygwin environment, jar does not like /cygdrive/c/... path - so 
         # convert to c:/... path.

         $filex = $file;
         $filex =~ s?^/cygdrive/([a-z])/?$1:/?;
         $tfilex = $tfile;
         $tfilex =~ s?^/cygdrive/([a-z])/?$1:/?;
         $errc = system ("native2ascii", "-encoding", "UTF-8", $filex, $tfilex);
         if ($errc)  {
            die "native2ascii -encoding UTF-8 $file $tfile: died with error code " . $errc/256 . "\nDied";
         }
         rename ($tfile, $file) or die "Cannot rename $tfile to $file: $!\nDied"; 
         $converted++;
      }
   }
   closedir FILES or die "Cannot close directory $dir: $!\nDied";
   unless ($converted)  {
      print "No files converted in $dir\n";
   }
   return;

}

# Subroutine duplicate_files
# 
# The first parameter is the directory in which to operate.  The second 
# parameter is the pattern of files to look for.  The third parameter is the
# pattern of locale tag to look for within those files.  The remaining 
# parameters are regexps of locales to duplicate: any file matching the first 
# two patterns when merged, and matching one of those regexps, is copied to
# each filename in the regexp that does not already exist.

sub duplicate_files  {

   local ($from_dir, $re1, $re2, @loc_res) = @_;
   local ($from_file, $to_file, $filename, $locs, $loc);
   local ($duplicated) = 0;

   # Merge regexps on which to search.  Eg, if re1 is ".*\.properties" and
   # re2 is "_en", then the merged regexp is ".*_en\.properties"

   $re1 =~ s/(\.[^\.]*)$/${re2}$1/;

   # Search for files matching merged regexp.  For each one found, compare
   # against list of regexps (which are assumed to be alternative, equivalent
   # locale codes like "he|iw".  If there is a match, split the regexp into
   # the individual locale codes, and copy the file to a new file for each 
   # one unless a file of that name already exists.
   
   opendir FILES, $from_dir or die "Cannot open directory $from_dir: $!\nDied";
   while (defined ($filename = readdir (FILES)))  {
      $from_file = "$from_dir/$filename";
      if ( -f $from_file && $filename =~ /$re1/i )  {
         foreach $locs (@loc_res)  {
            if ($filename =~ /_($locs)\.[^\.]*$/)  {
               foreach $loc (split (/\|/, $locs))  {
                  $to_file = $from_file;
                  $to_file =~ s/${re2}(\.[^\.]*)$/_${loc}$1/;
                  unless (-f $to_file)  {
                     print "Duplicating $from_file to $to_file\n";
                     copy ($from_file, $to_file) or die "Cannot copy $from_file to $to_file: $!\nDied";
                     $duplicated++;
                  }
               }
               last;
            }
         }
      }
   }
   closedir FILES or die "Cannot close directory $from_dir: $!\nDied";
   unless ($duplicated)  {
      print "No files duplicated in $from_dir\n";
   }
   return;
   
}

# 
# Subroutine normalize_files
#
# Besides directory in which to operate, this sub takes regexps.  The first
# regexp is the pattern of files to look for.  The second regexp is the pattern
# of locale tag to look for within those files.  The third regexp (optional)
# is the exception pattern.  Every file matching the first two patterns when
# merged, but not matching the exception pattern, the locale tag stripped from 
# it.  

sub normalize_files  {

   local ($from_dir, $re1, $re2, $re3) = @_;
   local ($from_file, $to_file, $filename);
   local ($renamed) = 0;

   # Merge regexps on which to search.  Eg, if re1 is ".*\.properties" and
   # re2 is "_en", then the merged regexp is ".*_en\.properties"

   $re1 =~ s/(\.[^\.]*)$/${re2}$1/;
   $re3 = "^\$" unless $re3;

   # Search for files matching merged regexp and rename them to match
   # unmerged regexp.  Eg if the merged regexp is ".*_en\.properties" and 
   # the matching file is "whatever_en.properties", rename it to 
   # whatever.properties.

   opendir FILES, $from_dir or die "Cannot open directory $from_dir: $!\nDied";
   while (defined ($filename = readdir (FILES)))  {
      $from_file = "$from_dir/$filename";
      if ( -f $from_file && $filename =~ /$re1/i && $filename !~ /$re3/i )  {
         $to_file = $from_file;
         $to_file =~ s/${re2}(\.[^\.]*)$/$1/;
         print "Renaming $from_file to $to_file\n";
         rename ($from_file, $to_file) or die "Cannot rename $from_file to $to_file: $!\nDied"; 
         $renamed++;
      }
   }
   closedir FILES or die "Cannot close directory $from_dir: $!\nDied";
   unless ($renamed)  {
      print "No files normalized in $from_dir\n";
   }
   return;

}

# 
# Subroutine remove_files
#
# Besides directory in which to operate, this sub takes regexps. The first
# array of regexps is death matches - if the file matches any in the list,
# it is removed.  The remaining arrays of regexps are life matches - if the 
# file matches any of the regexps in EACH array of life matches (after having 
# survived the death matches), it is retained (otherwise it is removed).
#

sub remove_files  {

   local ($from_dir, $death_patterns_ref, @life_patterns_refs) = @_;
   local ($file, $filename, $keep, $death_re, $life_re);
   local (@life_patterns, $life_patterns_ref);
   local (@death_patterns) = @$death_patterns_ref;
#  local (@life_patterns) = @$life_patterns_ref;
   local ($removed) = 0;

   opendir FILES, $from_dir or die "Cannot open directory $from_dir: $!\nDied";
   while (defined ($filename = readdir (FILES)))  {
      $file = "$from_dir/$filename";
      if ( -f $file )  {
         $keep = 1;
         foreach $death_re (@death_patterns)  {
            if ($filename =~ /$death_re/i)  {
               $keep = 0;
               last;
            }
         }
         if ($keep)  {
            foreach $life_patterns_ref (@life_patterns_refs)  {
               $keep = 0;
               @life_patterns = @$life_patterns_ref;
               foreach $life_re (@life_patterns)  {
                  if ($filename =~ /$life_re/i)  {
                     $keep = 1;
                     last;
                  }
               }
               last if !$keep;
            }
         }
         if (!$keep)  {
            print "Removing file $file\n";
            unlink $file or die "Cannot remove file $file: $!\nDied"; 
            $removed++;
         }
      }
   }
   closedir FILES or die "Cannot close directory $from_dir: $!\nDied";
   unless ($removed)  {
      print "No files removed from $from_dir\n";
   }
   return;

}

#
# Subroutine comment_files
#

sub comment_files  {

   local ($dir, @comment_these_msg_keys) = @_;
   local ($filename, $file, $keep, $commented_this_one);
   local ($commented_any_one) = 0;

   opendir FILES, $dir or die "Cannot open directory $dir: $!\nDied";
   while (defined ($filename = readdir (FILES)))  {
      $file = "$dir/$filename";

      # Only process properties files.  Comment-out unneeded message keys
      # from each property file.  If afterward there are no non-blank, 
      # uncommented message properties left in the file, remove it.

      if (( -f $file ) && ($file =~ /\.properties$/i))  {
         ($keep, $commented_this_one) = 
            &comment_file ($dir, $filename, @comment_these_msg_keys);
         unless ($keep)  {
            print "Removing now-empty file $file\n";
            unlink $file or die "Cannot remove file $file: $!\nDied";
         }
         $commented_any_one = $commented_any_one || $commented_this_one;
      }
   }
   unless ($commented_any_one)  {
      print "No files commented in $dir\n";
   }
   closedir FILES or die "Cannot close directory $dir: $!\nDied";
   return;

}

#
# Subroutine comment_file
#
# Returns two parameters: a counter of "good" lines (non-blank, non-comment), 
# and a counter of lines commented-out by this subroutine.

sub comment_file  {

   local ($dir, $filename, @comment_these_msg_keys) = @_;
   local ($has_msgs) = 0;
   local ($file) = "$dir/$filename";
   local ($tfile, $line, $msg_key);
   local ($commented) = 0;

   # Copy the file to a temp file.  As we copy, comment-out unneeded messages
   # and look for non-blank, uncommented, needed messages.

   open FILE, $file or die "Cannot open file $file: $!\nDied";
   ($TFILE, $tfile) = tempfile ("$file.XXXXXX");
   while ( $line = <FILE> )  {

      # Comment-out the line if it contains any of those message keys

      foreach $msg_key (@comment_these_msg_keys)  {
         if ($line =~ s/^(\s*$msg_key\=)/\#$1/)  {
            print "Commenting message(s) in file $file\n" unless $commented;
            $commented++;
            last;
         };
      }

      # Set flag if the line is not blank, commented-out, or blank value

      unless (($line =~ /^\s*$/) ||   # If not blank line AND
              ($line =~ /^\s*\#/) ||  #    not commented-out line AND
              ($line =~ /\=\s*$/))  { #    not line with blank message value
         $has_msgs++;                 # Then bump flag
      }
      print $TFILE $line or die "Cannot write to temporary file $tfile: $!\nDied";
   }
   close $TFILE or die "Cannot close temporary file $tfile: $!\nDied";
   close FILE or die "Cannot close file $file: $!\nDied";

   # Replace the original file with the temp file.  Return the flag indicating
   # whether the file has uncommented, needed, non-blank messages or not.

   rename ($tfile, $file) or die "Cannot rename temporary file $tfile to $file: $!\nDied";
   return ($has_msgs, $commented);

}

#
# Subroutine is_ms
#

sub is_ms {

   local $ms = system ("ver") ? 0 : 1;  # Try DOS VER command to discover if
                                        # this is a Microsoft platform
   return $ms;

}

1;

#!perl
###############################################################
#ident "@(#) REL xlate_receive.pl Release 3.0"
#ident "@(#) VER $Source: /external/Repo3/SP_FW/tools/admin_script/xlate_receive.pl,v $ $Header: /external/Repo3/SP_FW/tools/admin_script/xlate_receive.pl,v 1.6 2008/01/02 23:18:48 djorgen Exp $ $Locker:  $"
#ident "@(#) WHO Contact Scott Jorgenson <scott.jorgenson@hp.com>"
#ident "@(#) CR (C) Copyright Hewlett-Packard Company, 2009"
#
# Description: xlate_receive.pl : a perl script that automates the 
#              unpackaging and processing of translated SPF files prior to
#              their deployment to SPF
#
# xlate_receive.pl unpacks and processes translated portal and portlet files.
# It does its work in the BaseDir/ProjectName/CycleName/in_portal/* or 
# BaseDir/ProjectName/CycleName/in_portlet/* directory structure, as 
# appropriate.  It begins by unpacking every ZIP or TAR file found in the 'in'
# subdir, to the 'work/unpack' subdir.  Then it copies every text or media 
# file found in 'work/unpack' or 'in' to the 'work/text' or 'work/media' 
# subdir as appropriate.  Then it removes any non-translated files, and 
# normalizes locale-specific to locale-generic files.  Finally, it packs and/or
# copies the remaining files to the 'out' subdir.
#
# Note: xlate_receive.pl assumes that xlate_setup.pl has been run with
#       with the same ProjectName, CycleName and BaseDir to setup the 
#       needed directory structure.
#
# Usage: 
#
# First, put all your translated portal or portlet files into
# BaseDir/ProjectName/CycleName/in_portal/in or 
# BaseDir/CycleName/in_portlet/in as appropriate.  Put them unpacked or 
# packed in ZIP or TAR format.
#
# Then run the script:
#
#    xlate_receive.pl --p|project ProjectName
#                     --c|cycle CycleName
#                     --d|basedir BaseDir  # optional
#                     --portal | --portlet # required, mutually exclusive
#                     --text | --media     # required, not mutually exclusive
#
# a required parm --p|project ProjectName : required string specifying
#             project being translated, eg "SPF"
#
# a required parm --c|cycle CycleName : required string specifying translation
#             cycle, eg "R2_1"
#
# a required parm --portal or --portlet : controls whether to process portal
#             or portlet translated files (mutually exclusive)
#
# a required parm --text and/or --media : controls whether to process only
#             text files, media files, or both
#
# a required parm --d|basedir BaseDir : specifies the base directory 
#             containing the files to process for translation.
#             If not specified the current directory is used.
# 
# When the script finishes, your translated files ready to deploy to SPF will
# be in BaseDir/ProjectName/CycleName/in_portal/out or
# BaseDir/ProjectName/CycleName/in_portlet/out, as appropriate.
#
# History: 
#
# 1.0   05/05/07        GWM first version
# 2.0   06/22/07        DSJ overhaul
# 3.0   02/16/09        DSJ updated for Shared Portal Framework
#
################################################################
package main;

use Getopt::Long;
use File::Copy;
use Cwd;

#
# Global variables
#

#
# Set program defaults here
# 

#
# Process options:
#
 
$ok_opt = GetOptions (
             "p|project=s" => \$project_opt,  # project name
             "c|cycle=s" => \$cycle_opt,      # cycle name
             "d|basedir=s" => \$base_dir_opt, # base directory
	     "portal" => \$portal_opt,        # process portal files
	     "portlet" => \$portlet_opt,      # process portlet files
             "text" => \$text_opt,            # process textfiles
             "media" => \$media_opt,          # process mediafiles
             );
if (!$ok_opt) {
   &print_usage();
   exit 1;
}

#
# Start of xlate_send main -- get, check, set default args
#

$base_dir = &get_base_dir ($base_dir_opt);

# Require script library.
require "$base_dir/xlate_lib.pl";

$project =  &get_project ($project_opt);
$project_dir = &get_project_dir ($base_dir, $project);
$cycle = &get_cycle ($cycle_opt);
$cycle_dir = &get_cycle_dir ($base_dir, $project, $cycle);

if ($portal_opt && $portlet_opt) {
   &print_usage ();
   exit 1;
}
elsif (!$portal_opt && !$portlet_opt) {
   &print_usage ();
   exit 1;
}
$pdir = $portlet_opt ? "portlet" : "portal";

$text = $text_opt ? 1 : 0;
$media = $media_opt ? 1 : 0;
if (!$text_opt && !$media_opt) { 
   print "Will process text and media files by default.\n";
   $text = 1; 
   $media = 1; 
}

# First check $basedir and needed subdirectories

@check_dirs = ();
push (@check_dirs, $project_dir);
push (@check_dirs, $cycle_dir);
push (@check_dirs, $in_pdir = "$cycle_dir/in_$pdir");
push (@check_dirs, $in_in_pdir = "$in_pdir/in");
push (@check_dirs, $in_out_pdir = "$in_pdir/out");
push (@check_dirs, $in_work_pdir = "$in_pdir/work");
push (@check_dirs, $in_unpack_pdir = "$in_work_pdir/unpack");
if ($text) {
   push (@check_dirs, $in_text_pdir = "$in_work_pdir/text");
}
if ($media) {
   push (@check_dirs, $in_media_pdir = "$in_work_pdir/media");
}
&check_dirs ($base_dir, $project, $cycle, @check_dirs);

# Now, begin the work.  Process text and media files separately.

if ($text)  { 

   print <<EOF;
---------------------------------------------------
----- Unpacking and processing $pdir text files.
---------------------------------------------------
EOF

   # First, unpack any ZIP or TAR files found in the 'in' directory
   # to the 'unpack' directory.

   print "----- Unpacking $pdir packages.\n";
   &unpack_files ($in_in_pdir, $in_unpack_pdir);

   # Next copy text files to 'text' directory from both 'unpack' and 'in' dirs.
   # Why both places?  To pickup text files that had been staged to 'in' packed
   # or not.

   print "----- Gathering $pdir text files.\n";
   &copy_files ($in_unpack_pdir, $in_text_pdir, $all_text_files_re);
   &copy_files ($in_in_pdir, $in_text_pdir, $all_text_files_re);

   # Third, normalize locale-specific to locale-generic files.  This means
   # finding all files tagged with a country code (*_lc_CC.*) and stripping
   # the country code from it (unless the lc_CC is in an exception list).
   # Why are we doing this step?  See the Service Portal (OS) R2 localization 
   # architecture (HLD) document.  This script was taken fron the SP OS design.

   print "----- Normalizing country-specific to language-only translated files.\n";
   &normalize_files ($in_text_pdir, $all_xlated_text_files_re, 
                     "_[a-zA-Z]{2}", $all_nonnormal_xlated_files_re);

   # Fourth, convert non-ASCII characters in the properties files (which are all
   # assumed to be UTF8) to proper Java \u notation.  This means running Java
   # native2ascii utility against each property file.

   print "----- Converting non-ASCII message properties to Java Unicode syntax.\n";
   &convert_files ($in_text_pdir, $all_msg_text_files_re);

   # Portal only:

   if ($pdir eq "portal")  {
 
      # Next winnow text files down to just the translated versions of our 
      # application files (named spf-*.properties), project application files
      # (named ProjectName-*.properties), Vignette message property files
      # (named with Vignette GUID's), and misc text files (html, etc).  Only 
      # the translated versions of these files, not base versions or anything 
      # else.  Remove all the rest.

      print "----- Removing unneeded $pdir text files.\n";
      @death_patterns = ();
      @life_patterns_1 = ($all_xlated_files_re);
      @life_patterns_2 = ($all_vgn_portal_msg_files_re,
                          $all_spf_portal_msg_files_re,
                          $all_project_portal_msg_files_re,
                          $all_nonmsg_text_files_re);
      &remove_files ($in_text_pdir, \@death_patterns, 
                     \@life_patterns_1, \@life_patterns_2);

      # Last, ZIP the surviving message property files to the 'out' directory,
      # and just individually copy the remaining surviving text files to the
      # 'out' directory.

      print "----- Packing and staging remaining $pdir text files.\n";
      $zip_file = "$in_out_pdir/xlated-${pdir}-msgs-${cycle}.zip";
      &zip_files ($in_text_pdir, $zip_file, $all_msg_text_files_re);
      &copy_files ($in_text_pdir, $in_out_pdir, $all_nonmsg_text_files_re);

   }

   # Portlet only:

   if ($pdir eq "portlet")  {

      # Next, winnow text files down to just the translated versions, not base
      # versions or anything else.  Remove all the rest.

      print "----- Removing unneeded $pdir text files.\n";
      @death_patterns = ();
      @life_patterns_1 = ($all_xlated_files_re);
      @life_patterns_2 = ($all_text_files_re);
      &remove_files ($in_text_pdir, \@death_patterns, 
                      \@life_patterns_1, \@life_patterns_2);

      # Last, TAR the surviving text files to the 'out' directory.
      # Put the message properties in one TAR file, and the HTML in another.

      print "----- Packing remaining $pdir text files to TAR.\n";
      $tar_file = "$in_out_pdir/xlated-${pdir}-msgs-${cycle}.tar";
      &tar_files ($in_text_pdir, $tar_file, $all_msg_text_files_re);
      $tar_file = "$in_out_pdir/xlated-${pdir}-html-${cycle}.tar";
      &tar_files ($in_text_pdir, $tar_file, $all_nonmsg_text_files_re);
   }

   print <<EOF;
---------------------------------------------------
----- Done with $pdir text files.
---------------------------------------------------
EOF

}

if ($media)  {

   print <<EOF;
---------------------------------------------------
----- Unpacking and processing $pdir media files.
---------------------------------------------------
EOF

   # First, unpack any ZIP or TAR files found in the 'in' directory
   # to the 'unpack' directory.  Skip if already done for text, above.

   unless ($text)  {
      print "----- Unpacking $pdir packages.\n";
      &unpack_files ($in_in_pdir, $in_unpack_pdir);
   }

   # Next, copy media files to 'media' directory from both 'unpack' and 'in' 
   # dirs.  Why both places?  To pickup media files that had been staged to 
   # 'in' packed or not.

   print "----- Gathering $pdir media files.\n";
   &copy_files ($in_unpack_pdir, $in_media_pdir, $all_media_files_re);
   &copy_files ($in_in_pdir, $in_media_pdir, $all_media_files_re);

   # Third, normalize locale-specific to locale-generic files.  This means
   # finding all files tagged with a country code (*_lc_CC.*) and stripping
   # the country code from it (unless the lc_CC is in an exception list).
   # Why are we doing this step?  See the SP R2 localization architecture (HLD)
   # document.

   print "----- Normalizing country-specific to language-only translated files.\n";
   &normalize_files ($in_media_pdir, $all_xlated_media_files_re, 
                     "_[a-zA-Z]{2}", $all_nonnormal_xlated_files_re);

   # Next, winnow media files down to just the translated versions, not base
   # versions or anything else.  Remove all the rest.

   print "----- Removing unneeded $pdir media files.\n";
   @death_patterns = ();
   @life_patterns_1 = ($all_xlated_files_re);
   @life_patterns_2 = ($all_media_files_re);
   &remove_files ($in_media_pdir, \@death_patterns, 
                  \@life_patterns_1, \@life_patterns_2);

   # Portal only:
   # Just individually copy the remaining surviving media files to the
   # 'out' directory.

   if ($pdir eq "portal")  {
      print "----- Staging remaining $pdir media files.\n";
      &copy_files ($in_media_pdir, $in_out_pdir, $all_media_files_re);
   }

   # Portlet only:
   # Last, TAR the media files to the 'out' directory.

   if ($pdir eq "portlet")  {
      print "----- Packing remaining $pdir media files to TAR.\n";
      $tar_file = "$in_out_pdir/xlated-${pdir}-images-${cycle}.tar";
      &tar_files ($in_media_pdir, $tar_file);
   }

   print <<EOF;
---------------------------------------------------
----- Done with $pdir media files.
---------------------------------------------------
EOF

}

# Done.

exit 0;

#
# Subroutine print_usage
#

sub print_usage {

   print "Usage:\n";
   print "  xlate_send.pl --p|project ProjectName --c|cycle CycleName [--d|basedir BaseDir] --portal|portlet [--text|media]\n";
   return;

}

#
# Subroutine get_base_dir
#

sub get_base_dir  {

   local ($base_dir_opt) = @_;
   local ($base_dir);

   $base_dir = $base_dir_opt || ".";
   $base_dir =~ s/^\s*//g;      # Trim any leading whitespace
   $base_dir =~ s/[\s\/]*$//g;  # Trim any trailing whitespace or dir chars

   if (! -d $base_dir) { 
      print "Base directory $base_dir does not exist.\n";
      die;
   }

   # Change to base dir and normalize base_dir to absolute path

   chdir $base_dir or die "Unable to change directory to $base_dir:$!\nDied";
   $base_dir = cwd();
   return $base_dir;

}

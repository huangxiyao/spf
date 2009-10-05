#!perl
###############################################################
#ident "@(#) REL xlate_send.pl Release 3.0"
#ident "@(#) VER $Source: /external/Repo3/SP_FW/tools/admin_script/xlate_send.pl,v $ $Header: /external/Repo3/SP_FW/tools/admin_script/xlate_send.pl,v 1.13 2008/01/02 23:18:48 djorgen Exp $ $Locker:  $"
#ident "@(#) WHO Contact Scott Jorgenson <scott.jorgenson@hp.com>"
#ident "@(#) CR (C) Copyright Hewlett-Packard Company, 2009"
#
# Description: xlate_send.pl : a perl script that automates the processing
#              and packaging of base SPF resource files for translation
#
# xlate_send.pl processes and packs base portal and portlet resource files.
# It does its work in the BaseDir/ProjectName/CycleName/out_portal/* or 
# BaseDir/ProjectName/CycleName/out_portlet/* directory structure, as 
# appropriate.  It begins by unpacking every ZIP or TAR file found in the 'in'
# subdir, to the 'work/unpack/ subdir.  Then it copies every text or media file
# found in 'work/unpack' or 'in' to the 'work/text' or 'work/media' subdir as 
# appropriate.  Then it normalizes English to base Vignette message property 
# files, comments-out certain message properties not needing translation, and 
# removes any files not needing translation.  Finally, it packs the remaining
# files into TAR format in the 'out' subdir.
#
# Note: xlate_send.pl assumes that xlate_setup.pl has been run with
#       with the same ProjectName, CycleName and BaseDir to setup the 
#       needed directory structure.
#
# Usage: 
#
# First, put all your base portal or portlet files into
# BaseDir/ProjectName/CycleName/out_portal/in or 
# BaseDir/CycleName/out_portlet/in as appropriate.  Put them in unpacked, or 
# packed in ZIP or TAR format.
#
# Then run the script:
#
#    xlate_send.pl --p|project ProjectName
#                  --c|cycle CycleName
#                  --d|basedir BaseDir  # optional
#                  --portal | --portlet # required, mutually exclusive
#                  --text | --media     # required, not mutually exclusive
#
# a required parm --p|project ProjectName : required string specifying
#             project being translated, eg "SPF"
#
# a required parm --c|cycle CycleName : required string specifying translation
#             cycle, eg "R2_1"
#
# a required parm --portal or --portlet : controls whether portal files or 
#             portlet files will be translated (mutually exclusive)
#
# a required parm --text and/or --media : controls whether to process only
#             text files, media files, or both
#
# a required parm --d|basedir BaseDir : specifies the base directory 
#             containing the files to process for translation.
#             If not specified the current directory is used.
#
# When the script finishes, your base files packed and ready to send for
# translation will be in BaseDir/ProjectName/CycleName/out_portal/out or
# BaseDir/ProjectName/CycleName/out_portlet/out, as appropriate.
# 
# History: 
#
# 1.0   05/05/07        GWM first version
# 2.0   06/19/07        DSJ overhaul
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
push (@check_dirs, $out_pdir = "$cycle_dir/out_$pdir");
push (@check_dirs, $out_in_pdir = "$out_pdir/in");
push (@check_dirs, $out_out_pdir = "$out_pdir/out");
push (@check_dirs, $out_work_pdir = "$out_pdir/work");
push (@check_dirs, $out_unpack_pdir = "$out_work_pdir/unpack");
if ($text) {
   push (@check_dirs, $out_text_pdir = "$out_work_pdir/text");
}
if ($media) {
   push (@check_dirs, $out_media_pdir = "$out_work_pdir/media");
}
&check_dirs ($base_dir, $project, $cycle, @check_dirs);

# Now, begin the work.  Process text and media files separately.

if ($text)  { 

   print <<EOF;
---------------------------------------------------
----- Processing and packing $pdir text files.
---------------------------------------------------
EOF

   # First, unpack any ZIP or TAR files found in the 'in' directory
   # to the 'unpack' directory.

   print "----- Unpacking $pdir packages.\n";
   &unpack_files ($out_in_pdir, $out_unpack_pdir);

   # Now, copy text files to 'text' directory from both 'unpack' and 'in' dirs.
   # Why both places?  To pickup text files that had been staged to 'in' packed
   # or not.

   print "----- Gathering $pdir text files.\n";
   &copy_files ($out_unpack_pdir, $out_text_pdir, $all_text_files_re);
   &copy_files ($out_in_pdir, $out_text_pdir, $all_text_files_re);

   # Portal only:
   # Next, winnow text files down to just our framework application files
   # (named spf-*.properties), project application files (named
   # ProjectName*.properties), Vignette message property files
   # (named with Vignette GUID's), and misc text files (html, etc).  
   # Remove all the rest.

   if ($pdir eq "portal")  {
      print "----- Removing unneeded $pdir text files.\n";
      @death_patterns = ();
      @life_patterns = ($all_vgn_portal_msg_files_re,
                        $all_spf_portal_msg_files_re,
                        $all_project_portal_msg_files_re,
                        $all_nonmsg_text_files_re);
      &remove_files ($out_text_pdir, \@death_patterns, \@life_patterns);
   }

   # Portal only:
   # Next, comment-out unneeded messages from surviving message property 
   # files, and remove any resulting empty message property files.

   if ($pdir eq "portal")  {
      print "----- Commenting unneeded messages in $pdir message property files.\n";
      &comment_files ($out_text_pdir, @no_xlate_msg_keys);
   }

   # Next, normalize Vignette message property files from en (English) or
   # en_US (US English) to base versions. This means finding all *_en.properties
   # files named with Vignette GUID's (32 hex characters), and renaming them
   # without the _en code. This may overwrite existing base files, which is what
   # we want.  Then we find all *_en_US.properties files named with GUID's
   # likewise, and rename them to also lack the _en_US code, thus possibly 
   # overwriting base files or _en files.  Why are we doing this step?  See the
   # Service Portal (OS) R2 localization architecture (HLD) document (the design
   # for this script came from SP OS).
   #
   # Note: This step used to just apply to Vignette message property files.
   # Now we apply it to all files, for both portal and portlet.  DSJ 2009/4/3

   print "----- Normalizing English to base $pdir text files.\n";
   &normalize_files ($out_text_pdir, $all_text_files_re, "_en");
   print "----- Normalizing US-English to base $pdir text files.\n";
   &normalize_files ($out_text_pdir, $all_text_files_re, "_en_US");

   # Fifth, winnow text files down to just the base versions, not xlated
   # versions (if any).  Remove all the other files besides base text files.

   print "----- Removing unneeded $pdir text files.\n";
   @death_patterns = ($all_xlated_files_re);
   @life_patterns = ($all_text_files_re);
   &remove_files ($out_text_pdir, \@death_patterns, \@life_patterns);

   # Last, TAR the surviving text files to the 'out' directory.

   print "----- Packing remaining $pdir text files to TAR.\n";
   $tar_file = "$out_out_pdir/${project}_${cycle}-${pdir}-text.tar";
   &tar_files ($out_text_pdir, $tar_file);

   print <<EOF;
---------------------------------------------------
----- Done with $pdir text files.
---------------------------------------------------
EOF

}

if ($media)  {

   print <<EOF;
---------------------------------------------------
----- Processing and packing $pdir media files.
---------------------------------------------------
EOF

   # First, unpack any ZIP or TAR files found in the 'in' directory
   # to the 'unpack' directory.  Skip if already done for text, above.

   unless ($text)  {
      print "----- Unpacking $pdir packages.\n";
      &unpack_files ($out_in_pdir, $out_unpack_pdir);
   }

   # First copy media files to 'media' directory from both 'unpack' and 'in' 
   # dirs.  Why both places?  To pickup media files that had been staged to 
   # 'in' packed or not.

   print "----- Gathering $pdir media files.\n";
   &copy_files ($out_unpack_pdir, $out_media_pdir, $all_media_files_re);
   &copy_files ($out_in_pdir, $out_media_pdir, $all_media_files_re);

   # Third, normalize media files from en (English) or en_US (US English) to
   # base versions.  This means finding all *_en tagged media files and renaming
   # without the _en code. This may overwrite existing base files, which is what
   # we want.  Then we find all *_en_US tagged media files and rename them to
   # also lack the _en_US code, thus possibly overwriting base files or _en 
   # files.
   #
   # Note: This step used to just apply to Vignette message property files.
   # Now we apply it to all files, for both portal and portlet.  DSJ 2009/4/3

   print "----- Normalizing English to base $pdir media files.\n";
   &normalize_files ($out_media_pdir, $all_media_files_re, "_en");
   print "----- Normalizing US-English to base $pdir media files.\n";
   &normalize_files ($out_media_pdir, $all_media_files_re, "_en_US");

   # Third, winnow media files down to just the base versions, not xlated
   # versions (if any).  Remove all the rest.

   print "----- Removing unneeded $pdir media files.\n";
   @death_patterns = ($all_xlated_files_re);
   @life_patterns = ($all_media_files_re);
   &remove_files ($out_media_pdir, \@death_patterns, \@life_patterns);

   # Last, TAR the media files to the 'out' directory.

   print "----- Packing remaining $pdir media files to TAR.\n";
   $tar_file = "$out_out_pdir/${project}_${cycle}-${pdir}-media.tar";
   &tar_files ($out_media_pdir, $tar_file);

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

#!perl
###############################################################
#ident "@(#) REL xlate_setup.pl Release 3.0"
#ident "@(#) VER $Source: /external/Repo3/SP_FW/tools/admin_script/xlate_setup.pl,v $ $Header: /external/Repo3/SP_FW/tools/admin_script/xlate_setup.pl,v 1.10 2008/01/02 23:18:48 djorgen Exp $ $Locker:  $"
#ident "@(#) WHO Contact Scott Jorgenson <scott.jorgenson@hp.com>
#ident "@(#) CR (C) Copyright Hewlett-Packard Company, 2009"
#
# Description: xlate_setup.pl : a perl script that sets up the 
#              directories needed for the SPF translation process
#
# Usage: xlate_setup.pl --p|project ProjectName
#                       --c|cycle CycleName
#                       --d|basedir BaseDir # optional
#                       --cleanup_all       # optional
#                       --cleanup_work      # optional
#
# a required parm --p|project ProjectName : required string specifying 
#             project being translated, eg "SPF"
#
# a required parm --c|cycle CycleName : required string specifying translation
#             cycle, eg "R2_1"
#
# an optional parm --d|basedir BaseDir : specifies the base directory 
#             containing the files to process for translation.
#             If not specified the current directory is used.
# 
# an optional parm --cleanup_all : specifies to remove all files and subdirs
#             found in the directory structure besides the normal subdirs and
#             these perl scripts
#
# an optional parm --cleanup_work : like cleanup_all, but only applies to the
#             'work' directory trees
# 
# History: 
#
# 1.0   05/02/07        GWM first version
# 2.0   06/19/07        DSJ overhaul
# 3.0   02/16/09        DSJ updated for Shared Portal Framework
#
################################################################
package main;

use Getopt::Long;
use File::Copy;
use Cwd;

#
#  Global variables
#

#
# Set program defaults here -- so they can be overwritten by include file
# 

#
# Process options:
#
 
$ok_opt = GetOptions (
             "p|project=s" => \$project_opt,  # project name
             "c|cycle=s" => \$cycle_opt,      # cycle name
             "d|basedir=s" => \$base_dir_opt, # set base_dir for staging files
             "cleanup_all" => \$cleanup_all_opt,   # flag: cleanup_all
             "cleanup_work" => \$cleanup_work_opt  # flag: cleanup_work
             );
if (!$ok_opt) {
   &print_usage();
   exit 1;
}

#
# Start of xlate_setup main --  get, check, set default args
#

$base_dir = &get_base_dir ($base_dir_opt);

# Require script library.
require "$base_dir/xlate_lib.pl";

$project =  &get_project ($project_opt);
$project_dir = &get_project_dir ($base_dir, $project);
$cycle = &get_cycle ($cycle_opt);
$cycle_dir = &get_cycle_dir ($base_dir, $project, $cycle);

@make_dirs = ();

push (@make_dirs, $project_dir);
push (@make_dirs, $cycle_dir);

push (@make_dirs, $out_portal_dir = "$cycle_dir/out_portal");
push (@make_dirs, $out_portal_in_dir = "$out_portal_dir/in");
push (@make_dirs, $out_portal_out_dir = "$out_portal_dir/out");
push (@make_dirs, $out_portal_work_dir = "$out_portal_dir/work");
push (@make_dirs, $out_portal_unpack_dir = "$out_portal_work_dir/unpack");
push (@make_dirs, $out_portal_text_dir = "$out_portal_work_dir/text");
push (@make_dirs, $out_portal_media_dir = "$out_portal_work_dir/media");

push (@make_dirs, $in_portal_dir = "$cycle_dir/in_portal");
push (@make_dirs, $in_portal_in_dir = "$in_portal_dir/in");
push (@make_dirs, $in_portal_out_dir = "$in_portal_dir/out");
push (@make_dirs, $in_portal_work_dir = "$in_portal_dir/work");
push (@make_dirs, $in_portal_unpack_dir = "$in_portal_work_dir/unpack");
push (@make_dirs, $in_portal_text_dir = "$in_portal_work_dir/text");
push (@make_dirs, $in_portal_media_dir = "$in_portal_work_dir/media");

push (@make_dirs, $out_portlet_dir = "$cycle_dir/out_portlet");
push (@make_dirs, $out_portlet_in_dir = "$out_portlet_dir/in");
push (@make_dirs, $out_portlet_out_dir = "$out_portlet_dir/out");
push (@make_dirs, $out_portlet_work_dir = "$out_portlet_dir/work");
push (@make_dirs, $out_portlet_unpack_dir = "$out_portlet_work_dir/unpack");
push (@make_dirs, $out_portlet_text_dir = "$out_portlet_work_dir/text");
push (@make_dirs, $out_portlet_media_dir = "$out_portlet_work_dir/media");

push (@make_dirs, $in_portlet_dir = "$cycle_dir/in_portlet");
push (@make_dirs, $in_portlet_in_dir = "$in_portlet_dir/in");
push (@make_dirs, $in_portlet_out_dir = "$in_portlet_dir/out");
push (@make_dirs, $in_portlet_work_dir = "$in_portlet_dir/work");
push (@make_dirs, $in_portlet_unpack_dir = "$in_portlet_work_dir/unpack");
push (@make_dirs, $in_portlet_text_dir = "$in_portlet_work_dir/text");
push (@make_dirs, $in_portlet_media_dir = "$in_portlet_work_dir/media");

# Now setup and clean subdirectories as needed

while ($make_dir = shift @make_dirs)  {
   if (-d $make_dir)  { 
      if ($cleanup_all_opt)  { 
         &cleanup_dir ($make_dir);
      }
      elsif ($cleanup_work_opt)  {
         if ($make_dir =~ /\/(work|out)$/ || 
             $make_dir =~ /\/(work|out)\/[^\/]*$/)  {
            &cleanup_dir ($make_dir);
         }
      }
   }
   else  {
      print "Creating $make_dir\n";
      mkdir $make_dir or die "Cannot create $make_dir: $!\nDied";
   }

}

print <<EOF;
-----------------------
----- Setup complete.
-----------------------
EOF

exit 0;

#
# Subroutine print_usage
#

sub print_usage {

   print "Usage:\n";
   print "  xlate_setup.pl --p|project ProjectName --c|cycle CycleName [--d|basedir BaseDir] [--cleanup_all] [--cleanup_work]\n";
   return;

}

#
# Subroutine cleanup_dir
#

sub cleanup_dir {

   local ($dir) = @_;
   print "Cleaning $dir\n";

   opendir FILES, $dir or die "Cannot open directory $dir: $!\nDied";
   while (defined ($file = readdir (FILES)))  {
      $file = "$dir/$file";
      if (-f $file)  {
         unlink $file or print STDERR "Cannot remove file $file: $!\n";
      }
   }
   closedir FILES or die "Cannot close directory $dir: $!\nDied";

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
      die "Base directory $base_dir does not exist.\nDied";
   }

   # Change to base dir and normalize base_dir to absolute path

   chdir $base_dir or die "Unable to change directory to $base_dir:$!\nDied";
   $base_dir = cwd();
   return $base_dir;

}

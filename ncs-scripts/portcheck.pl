#!/usr/bin/perl
################################################################################
# @(#)$Id: portcheck.pl,v 1.2 2015/08/20 19:59:56 ttussing Exp $
#
# Program:  portcheck.pl
# Author:   Tim Tussing
# $Date: 2015/08/20 19:59:56 $
# Description: Simple port check program
#
#
#------------------------------------------------------------------------------
# $Revision: 1.2 $
# -----------------
# $Log: portcheck.pl,v $
# Revision 1.2  2015/08/20 19:59:56  ttussing
#         + Enh: Added error returned from socket on fail
#
# Revision 1.1  2013/10/18 17:55:46  ttussing
# Simple port checker. Initial Revision
#
#
################################################################################
use strict;
use warnings;
use vars qw /%opt/;
use Getopt::Std;
use IO::Socket::INET;

my $version='$Revision: 1.2 $';
my $program="";
my $target="g9u0079.houston.hp.com";
my $port="12345";

# usage statement
sub usage
{
   my $i;
   if (defined($_[0]))
   {
      $i=$_[0];
   }
   else
   {
      $i=1;
   }
   printf "Usage: $program
        -t       # Target host/ip
        -p       # Target port
        -h       # Syntax help                                 
        -v       # Version information\n\n"; 
    exit($i);
}

# Parse Arguments
sub parsearg
{
   my $i=0;
   getopts('vht:p:', \%opt) or usage(1);
   if ($opt{t}){
      $target = $opt{t};
   }
   if ($opt{p}){
      $port = $opt{p};
   }
   # Check for syntax help
   if ($opt{h}){
      usage(0);
   }
   if ($opt{v}){
      print "Version: $version\n";
      usage(0);
   }
}

#
# Main
#######################
parsearg();
my $sh = new IO::Socket::INET (
PeerHost => "$target",
PeerPort => "$port",
Proto => "tcp",
Timeout => 2
);
if ($sh)
{ 
   print "PASS: $target:$port\n";
   $sh->close();
}
else
{ 
   print "FAIL: $target:$port $!\n";
   exit(1);
}

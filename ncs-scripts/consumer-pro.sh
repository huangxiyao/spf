# OSSIRIS PRO
./portcheck.pl -t c2t02039.itcs.hp.com -p 8080 2>&1 | tee -a "portLog$(uname -n)austinhpcom.txt"
./portcheck.pl -t c1t02040.itcs.hp.com -p 8080 2>&1 | tee -a "portLog$(uname -n)austinhpcom.txt"

# OSSIRIS PRO LB
./portcheck.pl -t ossirisportlet.glb.itcs.hp.com -p 8080 2>&1 | tee -a "portLog$(uname -n)austinhpcom.txt"

# SPF producer PRO
./portcheck.pl -t spp-pro-uxportlet.austin.hp.com -p 7001 2>&1 | tee -a "portLog$(uname -n)austinhpcom.txt"
./portcheck.pl -t g1u1630c.austin.hp.com -p 21977 2>&1 | tee -a "portLog$(uname -n)austinhpcom.txt"
./portcheck.pl -t g2u0811c.austin.hp.com -p 21977 2>&1 | tee -a "portLog$(uname -n)austinhpcom.txt"

# LDAP
./portcheck.pl -t ed.hpicorp.net -p 389  2>&1 | tee -a "portLog$(uname -n)austinhpcom.txt"

# DB PRO
./portcheck.pl -t gvu3737.austin.hp.com -p 1525 2>&1 | tee -a "portLog$(uname -n)austinhpcom.txt"  
./portcheck.pl -t gvu3738.austin.hp.com -p 1525 2>&1 | tee -a "portLog$(uname -n)austinhpcom.txt"
./portcheck.pl -t gvu1391.austin.hp.com -p 1525 2>&1 | tee -a "portLog$(uname -n)austinhpcom.txt"
./portcheck.pl -t gvu1392.austin.hp.com -p 1525 2>&1 | tee -a "portLog$(uname -n)austinhpcom.txt"

./portcheck.pl -t g1u1676c.austin.hp.com -p 1525 2>&1 | tee -a "portLog$(uname -n)austinhpcom.txt"  
./portcheck.pl -t g1u1677c.austin.hp.com -p 1525 2>&1 | tee -a "portLog$(uname -n)austinhpcom.txt"  
./portcheck.pl -t g2u0063c.austin.hp.com -p 1525 2>&1 | tee -a "portLog$(uname -n)austinhpcom.txt"  
./portcheck.pl -t g2u0064c.austin.hp.com -p 1525 2>&1 | tee -a "portLog$(uname -n)austinhpcom.txt"

# HPP webservice PRO
./portcheck.pl -t hppws2.passport.hp.com    -p 443  2>&1 | tee -a "portLog$(uname -n)austinhpcom.txt"

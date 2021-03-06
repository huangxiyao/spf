# OSSIRIS ITG
./portcheck.pl -t c4t02041.itcs.hp.com -p 8080 2>&1 | tee -a "portLog$(uname -n)austinhpcom.txt"
./portcheck.pl -t c4t02042.itcs.hp.com -p 8080 2>&1 | tee -a "portLog$(uname -n)austinhpcom.txt"

# OSSIRIS ITG LB
./portcheck.pl -t ossirisportlettest.itcs.hp.com -p 8080 2>&1 | tee -a "portLog$(uname -n)austinhpcom.txt"

# SPF producer ITG
./portcheck.pl -t spp-itg-lb1-uxportlet.austin.hp.com -p 7001 2>&1 | tee -a "portLog$(uname -n)austinhpcom.txt"
./portcheck.pl -t g1u0754c.austin.hp.com -p 21977 2>&1 | tee -a "portLog$(uname -n)austinhpcom.txt"
./portcheck.pl -t g1u0755c.austin.hp.com -p 21977 2>&1 | tee -a "portLog$(uname -n)austinhpcom.txt"

# LDAP
./portcheck.pl -t ed.hpicorp.net -p 389  2>&1 | tee -a "portLog$(uname -n)austinhpcom.txt"

# DB ITG
./portcheck.pl -t gvu1393.austin.hp.com -p 1525 2>&1 | tee -a "portLog$(uname -n)austinhpcom.txt" 
./portcheck.pl -t gvu1394.austin.hp.com -p 1525 2>&1 | tee -a "portLog$(uname -n)austinhpcom.txt" 

./portcheck.pl -t g2u0065c.austin.hp.com -p 1525 2>&1 | tee -a "portLog$(uname -n)austinhpcom.txt" 
./portcheck.pl -t g2u0066c.austin.hp.com -p 1525 2>&1 | tee -a "portLog$(uname -n)austinhpcom.txt" 

# HPP webservice ITG
./portcheck.pl -t hppwsstg2.passport.hp.com -p 443  2>&1 | tee -a "portLog$(uname -n)austinhpcom.txt"

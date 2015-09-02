# Test web -> consumer
./portcheck.pl -t g1u1698.austin.hp.com -p 30027 2>&1 | tee -a "portLog$(uname -n)austinhpcom.txt"  
./portcheck.pl -t g1u1698.austin.hp.com -p 30029 2>&1 | tee -a "portLog$(uname -n)austinhpcom.txt"
./portcheck.pl -t g1u1699.austin.hp.com -p 30027 2>&1 | tee -a "portLog$(uname -n)austinhpcom.txt"
./portcheck.pl -t g1u1699.austin.hp.com -p 30029 2>&1 | tee -a "portLog$(uname -n)austinhpcom.txt"
./portcheck.pl -t g2u0170.austin.hp.com -p 30027 2>&1 | tee -a "portLog$(uname -n)austinhpcom.txt"
./portcheck.pl -t g2u0170.austin.hp.com -p 30029 2>&1 | tee -a "portLog$(uname -n)austinhpcom.txt"
./portcheck.pl -t g2u0172.austin.hp.com -p 30027 2>&1 | tee -a "portLog$(uname -n)austinhpcom.txt"
./portcheck.pl -t g2u0172.austin.hp.com -p 30029 2>&1 | tee -a "portLog$(uname -n)austinhpcom.txt"

# Test web -> siteminder
./portcheck.pl -t hpiprosm1.austin.hp.com -p 44001 2>&1 | tee -a "portLog$(uname -n)austinhpcom.txt"
./portcheck.pl -t hpiprosm1.austin.hp.com -p 44002 2>&1 | tee -a "portLog$(uname -n)austinhpcom.txt"
./portcheck.pl -t hpiprosm1.austin.hp.com -p 44003 2>&1 | tee -a "portLog$(uname -n)austinhpcom.txt"
./portcheck.pl -t hpiprosm2.austin.hp.com -p 44001 2>&1 | tee -a "portLog$(uname -n)austinhpcom.txt"
./portcheck.pl -t hpiprosm2.austin.hp.com -p 44002 2>&1 | tee -a "portLog$(uname -n)austinhpcom.txt"
./portcheck.pl -t hpiprosm2.austin.hp.com -p 44003 2>&1 | tee -a "portLog$(uname -n)austinhpcom.txt"

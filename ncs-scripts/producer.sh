# DB PRO
./portcheck.pl -t gvu3737.austin.hp.com -p 1525 2>&1 | tee -a "portLog$(uname -n)austinhpcom.txt"  
./portcheck.pl -t gvu3738.austin.hp.com -p 1525 2>&1 | tee -a "portLog$(uname -n)austinhpcom.txt"
./portcheck.pl -t gvu1391.austin.hp.com -p 1525 2>&1 | tee -a "portLog$(uname -n)austinhpcom.txt"
./portcheck.pl -t gvu1392.austin.hp.com -p 1525 2>&1 | tee -a "portLog$(uname -n)austinhpcom.txt"

./portcheck.pl -t g1u1676c.austin.hp.com -p 1525 2>&1 | tee -a "portLog$(uname -n)austinhpcom.txt"  
./portcheck.pl -t g1u1677c.austin.hp.com -p 1525 2>&1 | tee -a "portLog$(uname -n)austinhpcom.txt"  
./portcheck.pl -t g2u0063c.austin.hp.com -p 1525 2>&1 | tee -a "portLog$(uname -n)austinhpcom.txt"  
./portcheck.pl -t g2u0064c.austin.hp.com -p 1525 2>&1 | tee -a "portLog$(uname -n)austinhpcom.txt"

# DB ITG
./portcheck.pl -t gvu1393.austin.hp.com -p 1525 2>&1 | tee -a "portLog$(uname -n)austinhpcom.txt" 
./portcheck.pl -t gvu1394.austin.hp.com -p 1525 2>&1 | tee -a "portLog$(uname -n)austinhpcom.txt" 

./portcheck.pl -t g2u0065c.austin.hp.com -p 1525 2>&1 | tee -a "portLog$(uname -n)austinhpcom.txt" 
./portcheck.pl -t g2u0066c.austin.hp.com -p 1525 2>&1 | tee -a "portLog$(uname -n)austinhpcom.txt" 

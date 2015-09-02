# DB ITG
./portcheck.pl -t gvu1393.austin.hp.com -p 1525 2>&1 | tee -a "portLog$(uname -n)austinhpcom.txt" 
./portcheck.pl -t gvu1394.austin.hp.com -p 1525 2>&1 | tee -a "portLog$(uname -n)austinhpcom.txt" 

./portcheck.pl -t g2u0065c.austin.hp.com -p 1525 2>&1 | tee -a "portLog$(uname -n)austinhpcom.txt" 
./portcheck.pl -t g2u0066c.austin.hp.com -p 1525 2>&1 | tee -a "portLog$(uname -n)austinhpcom.txt" 

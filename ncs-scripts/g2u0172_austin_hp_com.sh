./portcheck.pl -t c0041754.itcs.hp.com -p 8080 2>&1 | tee -a portLogg2u0172austinhpcom.txt
./portcheck.pl -t c0044895.itcs.hp.com -p 8080 2>&1 | tee -a portLogg2u0172austinhpcom.txt
./portcheck.pl -t c2t02039.itcs.hp.com -p 8080 2>&1 | tee -a portLogg2u0172austinhpcom.txt
./portcheck.pl -t c4t02041.itcs.hp.com -p 8080 2>&1 | tee -a portLogg2u0172austinhpcom.txt
./portcheck.pl -t c4t02042.itcs.hp.com -p 8080 2>&1 | tee -a portLogg2u0172austinhpcom.txt

# link to LDAP
./portcheck.pl -t ed.hpicorp.net -p 389  2>&1 | tee -a portLogg2u0172austinhpcom.txt

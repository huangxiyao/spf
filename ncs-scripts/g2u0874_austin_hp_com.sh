# Test web -> consumer
./portcheck.pl -t g2u0057.austin.hp.com -p 30002 2>&1 | tee -a portLogg2u0057austinhpcom.txt
./portcheck.pl -t g2u0057.austin.hp.com -p 30004 2>&1 | tee -a portLogg2u0057austinhpcom.txt
./portcheck.pl -t g2u0058.austin.hp.com -p 30002 2>&1 | tee -a portLogg2u0058austinhpcom.txt
./portcheck.pl -t g2u0058.austin.hp.com -p 30004 2>&1 | tee -a portLogg2u0058austinhpcom.txt
./portcheck.pl -t g2u0059.austin.hp.com -p 30002 2>&1 | tee -a portLogg2u0059austinhpcom.txt
./portcheck.pl -t g2u0059.austin.hp.com -p 30004 2>&1 | tee -a portLogg2u0059austinhpcom.txt
./portcheck.pl -t g2u0148.austin.hp.com -p 30002 2>&1 | tee -a portLogg2u0148austinhpcom.txt
./portcheck.pl -t g2u0148.austin.hp.com -p 30004 2>&1 | tee -a portLogg2u0148austinhpcom.txt

# Test web -> siteminder
./portcheck.pl -t hpiitgsm1.austin.hp.com -p 44001 2>&1 | tee -a portLogghpiitgsm1austinhpcom.txt
./portcheck.pl -t hpiitgsm1.austin.hp.com -p 44002 2>&1 | tee -a portLogghpiitgsm1austinhpcom.txt
./portcheck.pl -t hpiitgsm1.austin.hp.com -p 44003 2>&1 | tee -a portLogghpiitgsm1austinhpcom.txt
./portcheck.pl -t hpiitgsm2.austin.hp.com -p 44001 2>&1 | tee -a portLogghpiitgsm2austinhpcom.txt
./portcheck.pl -t hpiitgsm2.austin.hp.com -p 44002 2>&1 | tee -a portLogghpiitgsm2austinhpcom.txt
./portcheck.pl -t hpiitgsm2.austin.hp.com -p 44003 2>&1 | tee -a portLogghpiitgsm2austinhpcom.txt

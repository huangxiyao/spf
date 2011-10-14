In order for validation to work, a security certificate must be installed.
The following certificate is from the Enterprise Directory web site:

http://directory.hp.com/prog/jcert.html

At the time of this writing:
	http://directory.hp.com
	Select link to "Sample Code" http://directory.hp.com/prog/sample_code.html
	Under Java Sample Code heading there is a "More" link to the certificate page above
	
Copy the certificate from that page and put it in a temporary file such as /tmp/ldap.cer

The certificate must now be installed into the JVM using the commands:

cd JAVA_HOME/jre/lib/security
keytool -import -file /tmp/ldap.cer -keystore cacerts -alias hpca
	password is changeit

To check the status of the certificate on the local system:
keytool -list -v -keystore cacerts -alias hpca

-----BEGIN CERTIFICATE-----
MIIDnDCCAwWgAwIBAgIQY7cb6SerTGH4X5A+DRfexjANBgkqhkiG9w0BAQUFADCB
njEPMA0GA1UEChMGaHAuY29tMRowGAYDVQQLExFJVCBJbmZyYXN0cnVjdHVyZTEL
MAkGA1UEBhMCVVMxIDAeBgNVBAoTF0hld2xldHQtUGFja2FyZCBDb21wYW55MUAw
PgYDVQQDEzdIZXdsZXR0LVBhY2thcmQgUHJpbWFyeSBDbGFzcyAyIENlcnRpZmlj
YXRpb24gQXV0aG9yaXR5MB4XDTk3MTIzMDAwMDAwMFoXDTEyMTIyOTIzNTk1OVow
gZ4xDzANBgNVBAoTBmhwLmNvbTEaMBgGA1UECxMRSVQgSW5mcmFzdHJ1Y3R1cmUx
CzAJBgNVBAYTAlVTMSAwHgYDVQQKExdIZXdsZXR0LVBhY2thcmQgQ29tcGFueTFA
MD4GA1UEAxM3SGV3bGV0dC1QYWNrYXJkIFByaW1hcnkgQ2xhc3MgMiBDZXJ0aWZp
Y2F0aW9uIEF1dGhvcml0eTCBnzANBgkqhkiG9w0BAQEFAAOBjQAwgYkCgYEA1Sd+
1f2Lvr0gCSSLV9aK2cf4o0rKmleBxYitiXGQNJbPe+e5mo9Qn0/LIeS2fg27mlx8
nZJEaHhe5ozWNggalWJbiNrkKIOmXDkJBu3aqWG+b3kNzPErJ5YK/q+4fSwzvubR
7GS72y7lzaRD+fDKJb8lvQujV7LP5jBUdiWmE98CAwEAAaOB2DCB1TAnBgNVHREE
IDAepBwwGjEYMBYGA1UEAxMPUHJpdmF0ZUxhYmVsMS04MB0GA1UdDgQWBBRKVh+8
ilGP9IMTydpceVTIArqrnzASBgNVHRMBAf8ECDAGAQH/AgEAMAsGA1UdDwQEAwIB
JjARBglghkgBhvhCAQEEBAMCAAcwNgYJYIZIAYb4QgENBCkWJ1RoaXMgYXV0aG9y
aXR5IGlzIGZvciBIUCBidXNpbmVzcyBvbmx5LjAfBgNVHSMEGDAWgBRKVh+8ilGP
9IMTydpceVTIArqrnzANBgkqhkiG9w0BAQUFAAOBgQDDzV15K84ZqShBn8yS3gsn
uMQKnL41oxQq6Pq2PtngxqoylPrbnXy4W/iPp+ycVfLTKCCtOLb47R2vK6OINZHf
SnxZOoxFXj7HXejOoWs12GmNiLlOfBbWX3bRDjkGrX1hywUfZynhW1NALzLDXfi3
RzFJW8ItZGAw65NR2iEyAg==
-----END CERTIFICATE-----
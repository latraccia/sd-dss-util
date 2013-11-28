# DSS-CLI
---------

## Usage

To sign a document, simply call the `SignCLI` class on the `dss-cli-main.jar` with the proper parameters:

    java dss-cli-main.jar SignCli

### Parameters
The following list contains all of the accepted parameters.

* input file, passed as first parameter
* `--destination` or `-d`, the destination path for the signed file (or folder)
* `--format` or `-f`, the signature format
	* `CAdES`
	* `PAdES`, for PDF files only
	* `XAdES`
	* `ASiC-S`
* `--level` or `-l`, the signature level
	* `BES`, available for every format
	* `EPES`, available for every format
	* `LTV`, only for `PAdES`
	* `T`, only for `CAdES`, `XAdES`, `ASiC-S`
	* `C`, only for `CAdES`, `XAdES`
	* `X`, only for `CAdES`, `XAdES`
	* `XL`, only for `CAdES`, `XAdES`
	* `A`, only for `CAdES`, `XAdES`
* `--packaging` or `-p`, the packaging kind for the signed file(s)
	* `ENVELOPING`, only for `CAdES`, `XAdES`
	* `ENVELOPED`, only for `PAdES` (PDF files), `XAdES` (XML files)
	* `DETACHED`, only for `CAdES`, `XAdES`
* `--pkcs11` or `-p11`, specified if the signature token is provided via PKCS#11. It has to reference the library path for the SSCD (e.g. smart card)
* `--pkcs12` or `-p12`, specified if the signature token is provided via PKCS#12. It has to reference both the file path and the password the file is encrypted with
* `--mscapi` or `-ms` with no parameters, if the signature token is MS CAPI
* `--mocca` or `-m` if the signature token is provided via MOCCA:
	* `SHA1`
	* `SHA256`
* `--signer-role` or `-sr`, the claimed role of the signer, optional
* `--signature-policy-implicit` or `-spi`, optional, if there is a signature policy to be implicitly inferred from the certificate/token
* `--signature-policy-explicit` or `-spe`, optional, if the policy is specified with the following required data:
	* the policy OID as a dot-separated digits string
	* the hash value of the signature policy in base64 format
	* the algorithm used to produce it (only available option for now is `SHA1`)

### Example

    java dss-cli-main.jar SignCli "input/file.pdf"
		-d="output/file.pdf"
		-f=CAdES
		-l=BES
		-p=ENVELOPING
		-p12="path/to/the/pkcs12/key.p12" "pkcs12filepassword"
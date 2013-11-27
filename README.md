# DSS-CLI
---------

## Usage

### Parameters
The following list contains all of the accepted parameters.

* input file, passed as first parameter
* `-d` or `--destination`, the destination path for the signed file (or folder)
* `-f` or `--format`, the signature format
	* `CAdES`
	* `PAdES`, for PDF files only
	* `XAdES`
	* `ASiC-S`
* `-l` or `--level`, the signature level
	* `BES`, available for every format
	* `EPES`, available for every format
	* `LTV`, only for `PAdES`
	* `T`, only for `CAdES`, `XAdES`, `ASiC-S`
	* `C`, only for `CAdES`, `XAdES`
	* `X`, only for `CAdES`, `XAdES`
	* `XL`, only for `CAdES`, `XAdES`
	* `A`, only for `CAdES`, `XAdES`
* `-p` or `--packaging`, the packaging kind for the signed file(s)
	* `ENVELOPING`, only for `CAdES`, `XAdES`
	* `ENVELOPED`, only for `PAdES` (PDF files), `XAdES` (XML files)
	* `DETACHED`, only for `CAdES`, `XAdES`
* `-p11` or `--pkcs11`, specified if the signature token is provided via PKCS#11. It has to reference the library path for the SSCD (e.g. smart card)
* `-p12` or `--pkcs12`, specified if the signature token is provided via PKCS#12. It has to reference both the file path and the password the file is encrypted with
* `-ms` or `--mscapi` with no parameters, if the signature token is MS CAPI
* `-mo` or `--mocca` if the signature token is provided via MOCCA:
	* `SHA1`
	* `SHA256`

### Example

    java dss-cli-main.jar SignCli "input/file.pdf"
		-d="output/file.pdf"
		-f=CAdES
		-l=BES
		-p=ENVELOPING
		-p12="path/to/the/pkcs12/key.p12" "pkcs12filepassword"
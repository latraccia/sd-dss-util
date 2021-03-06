# SD-DSS-Util [![Build Status](https://travis-ci.org/latraccia/sd-dss-util.png?branch=develop)](https://travis-ci.org/latraccia/sd-dss-util)
*A Utility library and a Command Line Interface for SD-DSS.*

## Introduction

### SD-DSS-Util

SD-DSS-Util is a utility library for making the signing process easier. The library implements the builder pattern to
guide the developer through the process.

It is based on the DSS Applet you can find in the
[original sources of DSS](https://joinup.ec.europa.eu/software/sd-dss/release/all).

### SD-DSS-CLI

SD-DSS-CLI is a Command Line Interface for handling communication with a DSS server, and therefore signing, validating
and extending documents.

### SD-DSS

The DSS software allows Member States to create and verify X/CAdES forms up to the `-A` form and PAdES forms up to
`LTV` originating from other MS when used with documents.

In particular it:

* supports the requirements in Commission Decision 2011/130/EU;
* for verification makes use of Member States' trusted lists;
* is available under the form of an SDK and as a standalone application.
* allows easy use of the MOCCA adapter to increase the support of Ms smalrtcards at the signing aide

For more information about SD-DSS, please refer to the [official page](https://joinup.ec.europa.eu/software/sd-dss).

## How to build

The project is based on `pom.xml` files, so you need to have Maven installed and configured.

`SD-DSS` is available on Maven since `3.0.3`, so you can simply call, after cloning the repo:

```shell
    maven clean install
```

This will proceed on the main `pom.xml` to clean and build the project.

**Important:** SD-DSS-CLI is currently tested on SD-DSS v3.0.3.

## How to use

To sign a document, simply call the `SignCLI` class on the `sd-dss-util-cli.jar` with the proper parameters:

    java sd-dss-util-cli.jar SignCli

### Parameters
The following list contains all of the accepted parameters.

* input file, passed as first nameless parameter
* **REQUIRED** `--url` or `-u`, the DSS server URL, required for `PAdES` (all levels) and for levels other than `BES`,
`EPES` in other formats
* `--output` or `-o`, the destination path (optionally, with a file name)
* **REQUIRED** `--format` or `-f`, required, the signature format
	* `CAdES`
	* `PAdES`, for PDF files only
	* `XAdES`
	* `ASiC-S`, not yet implemented
* **REQUIRED** `--level` or `-l`, required, the signature level
	* `BES`, available for every format
	* `EPES`, available for every format
	* `LTV`, only for `PAdES`
	* `T`, only for `CAdES`, `XAdES`, `ASiC-S`
	* `C`, only for `CAdES`, `XAdES`
	* `X`, only for `CAdES`, `XAdES`
	* `XL`, only for `CAdES`, `XAdES`
	* `A`, only for `CAdES`, `XAdES`
* **REQUIRED** `--packaging` or `-p`, required, the packaging kind for the signed file(s)
	* `ENVELOPING`, only for `CAdES`, `XAdES`
	* `ENVELOPED`, only for `PAdES` (PDF files), `XAdES` (XML files)
	* `DETACHED`, only for `CAdES`, `XAdES`
* `--digest-algorithm` or `-d`, the digest algorithm used for signing, optional and unused if `--mocca` is set
	* `SHA1`, default
	* `SHA256`
	* `SHA512`
* `--pkcs11` or `-p11`, specified if the signature token is provided via PKCS#11. It has to reference both the library
path for the SSCD (e.g. smart card) and the password the card is encrypted with. The library path can be absolute or
relative to the `jar` resources folder
* `--pkcs12` or `-p12`, specified if the signature token is provided via PKCS#12. It has to reference both the file
path and the password the file is encrypted with. The file path can be absolute or relative to the `jar` resources
folder
* `--mscapi` or `-ms` with no parameters, if the signature token is MS CAPI
* `--mocca` or `-m` if the signature token is provided via MOCCA:
	* `SHA1`
	* `SHA256`
* `--issuercn` or `-icn`, a regular expression that will be used to select the right key based on the certificate issuer CN; if there are multiple keys and the issuer CN is not specified, a key selection will be prompted
* `--signer-role` or `-sr`, the claimed role of the signer, optional
* `--signature-policy-explicit` or `-spe`, optional, if the policy is specified with the following required data:
	* the policy OID as a dot-separated digits string
	* the hash value of the signature policy in base64 format
	* the algorithm used to produce it (only available option for now is `SHA1`)
* `--simulate` or `-s`, validates the parameters but doesn't start the actual signing process
* `--log` or `-log`, the path of the log file to be written (if any)

Even if `--pkcs11`, `--pkcs12`, `--mscapi`, `--mocca` are not required, one of them has to be specified in order to
provide the signature token provider.

### Example

    java sd-dss-util-cli.jar SignCli
		"input/file.pdf"
		--format=CAdES
		--level=BES
		--packaging=ENVELOPING
		--pkcs12="path/to/the/pkcs12/key.p12" "pkcs12filepassword"
		--output="output/file.pdf"
		--url="http://localhost:8080/service"
		--icn=".*some.key.provider.*"
		
### Logging

The `--log` option will create a "utf-8" log file with the following content pattern:

    [code]
    [description]

The code can be one of the following:

* `0`: success!
* `-1` SignatureSourceFileNotFoundException: The source file could not be found.
* `-2` SignatureFormatMismatchException: The selected signature format collides with the file type.
* `-3` SignatureLevelMismatchException: The selected signature level collides with other options.
* `-4` SignaturePackagingMismatchException: The selected signature packaging collides with the file type.
* `-5` SignatureServiceUrlException: The selected service URL is not available.
* `-6` SignaturePolicyAlgorithmMismatchException: The selected explicit policy algorithm is not available.
* `-7` SignaturePolicyLevelMismatch: The selected signature level collides with the policy options.
* `-8` SignatureTokenException: The PKCS12 private key could not be found.
* `-9` KeyStoreException: The selected keystore failed (device not connected, no drivers, etc.).
* `-10` BadPasswordException: The password was not valid.
* `-11` NoSuchAlgorithmException: The selected digest algorithm could not be loaded.
* `-12` SignatureTargetFileException} The target output file could not be written.
* `-13` For any other exception.

The description (if any) will contain the stack trace of the Exception (for reporting purposes).

## Future development and contribution

SD-DSS-Util and CLI aim to be implementation references for DSS clients, thus more work will be done to:

* unit-test the whole project and all of the use cases
* extend document signatures
* validate document signatures

Pull requests and any kind of contribution are welcomed.

## License

SD-DSS-Util is released under the LGPL.

    SD-DSS-Util, a Utility Library and a Command Line Interface for SD-DSS.
    Copyright (C) 2014 La Traccia http://www.latraccia.it/en/
    Developed by Francesco Pontillo

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see [http://www.gnu.org/licenses/].

### Authors

The original **SD-DSS** project has been commissioned by the European Commission (DG MARKT, Directorate E, Services),
financed under the ISA Work Programme (DG DIGIT) in the framework of the implementation of Services Directive.

**SD-DSS-Util** has been developed by [Francesco Pontillo](mailto:francescopontillo@gmail.com)
([La Traccia](http://www.latraccia.it/en/)).

### Used libraries

SD-DSS-Util directly uses the following libraries/modules:

* [**SD-DSS**](https://joinup.ec.europa.eu/software/sd-dss), developed by
[**ARHS Developments S.A.**](http://www.arhs-developments.com) (rue Nicolas Bové 2B, L-1253 Luxembourg), released by
2011 European Commission, Directorate-General Internal Market and Services (DG MARKT), B-1049 Bruxelles/Brussel
under **LGPL v3**.
* [**JCommander**](http://jcommander.org/), developed by [**Cédric Beust**](mailto:cedric@beust.com), released under
**Apache 2.0 license**.
* [**Apache Commons IO**](http://commons.apache.org/proper/commons-io/), developed by
[**Apache**](http://www.apache.org/), released under **Apache 2.0 license**.

You can find a list of referenced libraries' licenses in the `licenses` directory and the related notices in the
`NOTICE` file in the root of the project.
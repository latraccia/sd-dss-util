# DSS-CLI
-------
*A Command Line Interface for SD-DSS.*


## Introduction

### SD-DSS CLI

SD-DSS CLI is a Command Line Interface for handling communication with a DSS server, and therefore signing, validating and extending documents.

It is based on the DSS Applet you can find in the [original sources of DSS](https://joinup.ec.europa.eu/software/sd-dss/release/all).

### SD-DSS

The DSS software allows Member States to create and verify X/CAdES forms up to the `-A` form and PAdES forms up to `LTV` originating from other MS when used with documents.

In particular it:

* supports the requirements in Commission Decision 2011/130/EU;
* for verification makes use of Member States' trusted lists;
* is available under the form of an SDK and as a standalone application.
* allows easy use of the MOCCA adapter to increase the support of Ms smalrtcards at the signing aide

For more information about SD-DSS, please refer to the [official page](https://joinup.ec.europa.eu/software/sd-dss).

## How to build

The project is based on `pom.xml` files, so you need to have Maven installed and configured.

DSS-CLI references, in its main `pom.xml` the module `submods/dss/dss-src`; this means you need to include the version of SD-DSS you want to use inside the `submods` directory. To do so, you may consider using a git submodule. This kind of reference will probably be removed as soon as the [DSS is released on a maven repository](https://joinup.ec.europa.eu/software/sd-dss/topic/how-contribute#comment-15030), and will be replaced by a regular maven module reference.

Then, simply call `maven clean install` on the main `pom.xml` to clean and build the project.

**Important:** the project is structured to allow you to change the original SD-DSS sources in order to comply with the LGPL v3, but DSS-CLI is currently tested on SD-DSS v2.0.2.

## How to use

To sign a document, simply call the `SignCLI` class on the `dss-cli-main.jar` with the proper parameters:

    java dss-cli-main.jar SignCli

### Parameters
The following list contains all of the accepted parameters.

* input file, passed as first nameless parameter
* `--output` or `-o`, the destination path (optionally, with a file name)
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
* `--digest-algorithm` or `-d`, the digest algorithm used for signing, optional and unused if `--mocca` is set
	* `SHA1`, default
	* `SHA256`
	* `SHA512`
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
* `--simulate` or `-s`, validates the parameters but doesn't start the actual signing process

### Example

    java dss-cli-main.jar SignCli
		"input/file.pdf"
		--format=CAdES
		--level=BES
		--packaging=ENVELOPING
		--pkcs12="path/to/the/pkcs12/key.p12" "pkcs12filepassword"
		--output="output/file.pdf"

## Future development and contribution

DSS-CLI aims to be an implementation reference for DSS clients, thus more work will be done to:

* unit-test the whole project
* generalize the signing behaviour in a library
* extend document signatures
* validate document signatures

Pull requests and any kind of contribution are welcomed.

## License

DSS-CLI is released under the LGPL.

    DSS-CLI, a Command Line Interface for SD-DSS.
    Copyright (C) 2013 La Traccia
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

The original **SD-DSS** project has been commissioned by the European Commission (DG MARKT, Directorate E, Services), financed under the ISA Work Programme (DG DIGIT) in the framework of the implementation of Services Directive.

**DSS-CLI** has been developed by [Francesco Pontillo](mailto:francescopontillo@gmail.com) ([La Traccia](http://www.latraccia.it/en/)).

### Used libraries

DSS-CLI directly uses the following libraries/modules:

* [**SD-DSS**](https://joinup.ec.europa.eu/software/sd-dss), developed by [**ARHS Developments S.A.**](http://www.arhs-developments.com) (rue Nicolas Bové 2B, L-1253 Luxembourg), released by 2011 European Commission, Directorate-General Internal Market and Services (DG MARKT), B-1049 Bruxelles/Brussel under **LGPL v3**.
* [**JCommander**](http://jcommander.org/), developed by [**Cédric Beust**](mailto:cedric@beust.com), released under **Apache 2.0 license**.

You can find a list of referenced libraries' licenses in the `licenses` directory and the related notices in the `NOTICE` file in the root of the project.
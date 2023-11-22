			byte[] signature = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
			Org.BouncyCastle.Crypto.IDigest digest = new Org.BouncyCastle.Crypto.Digests.Sha256Digest();
			digest.BlockUpdate(signature, 0, signature.Length);
			byte[] signatureDigest = new byte[digest.GetDigestSize()];
			int outOff = 0;
			digest.DoFinal(signatureDigest, outOff);

			Org.BouncyCastle.Tsp.TimeStampRequestGenerator tsRequestGenerator = new Org.BouncyCastle.Tsp.TimeStampRequestGenerator(); // certificate generator
			tsRequestGenerator.SetCertReq(true);
			Org.BouncyCastle.Tsp.TimeStampRequest tsRequest = tsRequestGenerator.Generate(Org.BouncyCastle.Tsp.TspAlgorithms.Sha256, signatureDigest); // vygenerujeme request

			Timestamp ts = new Timestamp();
			byte[] responseBytes = ts.GetTimestamp(tsRequest.GetEncoded(), "https://test.ditec.sk/TSAServer/tsa.aspx");

			Org.BouncyCastle.Tsp.TimeStampResponse tsResponse = new Org.BouncyCastle.Tsp.TimeStampResponse(responseBytes);




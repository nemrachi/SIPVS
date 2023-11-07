using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Text;

namespace WindowsFormsApp1
{
	public class Timestamp
	{

		public Timestamp()
		{
		}

		private const string TS_QUERY_MIME_TYPE = "application/timestamp-query";
		private const string TS_REPLY_MIME_TYPE = "application/timestamp-reply";

		private string errorMessage = "OK";

		/// <summary>
		/// Get Timestamp
		/// </summary>
		/// <param name="tsRequest"></param>
		/// <returns></returns>
		public byte[] GetTimestamp(byte[] tsRequest, string tsUrl)
		{
			try
			{
				HttpWebRequest req = (HttpWebRequest)WebRequest.Create(tsUrl);

				req.ServerCertificateValidationCallback +=
					(sender, cert, chain, error) =>
						{
							return true;
						};


				req.Method = "POST";
				req.ContentType = TS_QUERY_MIME_TYPE;
				req.ContentLength = tsRequest.Length;

				Stream requestStream = req.GetRequestStream();
				requestStream.Write(tsRequest, 0, tsRequest.Length);
				requestStream.Close();

				using (HttpWebResponse res = (HttpWebResponse)req.GetResponse())
				{
					//verify response header
					if (res.ContentType.ToLower() != TS_REPLY_MIME_TYPE.ToLower())
					{
						throw new Exception("incorrect response mimetype. " + res.ContentType);
					}

					using (Stream stm = new BufferedStream(res.GetResponseStream()))
					{
						stm.Flush();
						using (MemoryStream ms = new MemoryStream())
						{
							stm.CopyTo(ms);
							return ms.ToArray();
						}
					}
				}
			}
			catch (Exception ex)
			{
				this.errorMessage = ex.ToString();
				return null;
			}
		}

		/// <summary>
		/// error message property
		/// </summary>
		public string ErrorMessage
		{
			get
			{
				return this.errorMessage;
			}
		}
	}
}

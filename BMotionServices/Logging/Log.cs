using System;
using System.Collections.Generic;
using System.Configuration;
using System.IO;
using System.Linq;
using System.Web;

namespace BMotionServices.Logging
{
    public class Log
    {
        static Log log = null;
        private string path = ConfigurationManager.AppSettings["LogError"];
        public static Log getInstance()
        {
            if (log == null)
            {
                log = new Log();
                return log;
            }
            else
            {
                return log;
            }
        }

        public void CreateLogError(Exception e, string payload)
        {
            string date = DateTime.Now.ToShortDateString().Replace("/","-");
            string message = string.Format("Time: {0}", DateTime.Now.ToString("dd/MM/yyyy hh:mm:ss tt"));
            message += Environment.NewLine;
            message += "------------------------------------------------------------------------";
            message += Environment.NewLine;
            message += string.Format("Message: {0}", e.Message);
            message += Environment.NewLine;
            message += string.Format("StackTrace: {0}", e.StackTrace);
            message += Environment.NewLine;
            message += string.Format("Source: {0}", e.Source);
            message += Environment.NewLine;
            message += string.Format("TargetSite: {0}", e.TargetSite);
            message += Environment.NewLine;
            message += string.Format("Payload: {0}", payload);
            message += Environment.NewLine;
            message += "------------------------------------------------------------------------";
            message += Environment.NewLine;

            using (StreamWriter writer = new StreamWriter(path + date + ".txt", true))
            {
                writer.WriteLine(message);
                writer.Close();
            }
        }

        public void CreateConsoleLog(string value)
        {
            string date = DateTime.Now.ToShortDateString().Replace("/","-");
            using (StreamWriter writer = new StreamWriter(path + date + ".txt", true))
            {
                writer.WriteLine(value);
                writer.Close();
            }
        }
    }
}
using BMotionServices.Entity;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace BMotionServices.Parse
{
    public class ResponseInfo
    {
        public string status { get; set; }
        public string message { get; set; }
        public List<sp_Notification_Result> Data { get; set; }
    }
}
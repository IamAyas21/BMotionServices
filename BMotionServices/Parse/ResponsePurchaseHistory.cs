using BMotionServices.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace BMotionServices.Parse
{
    public class ResponsePurchaseHistory
    {
        public string status { get; set; }
        public string message { get; set; }
        public List<PurchaseHistory> Data { get; set; }
    }
}
using BMotionServices.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace BMotionServices.Parse
{
    public class ResponseOrders
    {
        public string status { get; set; }
        public string message { get; set; }
        public Orders Data { get; set; }
    }
}
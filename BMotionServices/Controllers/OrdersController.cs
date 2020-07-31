using BMotionServices.Logic;
using BMotionServices.Models;
using BMotionServices.Parse;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace BMotionServices.Controllers
{
    //[Authorize]
    [RoutePrefix("api/Orders")]
    public class OrdersController : ApiController
    {
        [HttpPost]
        [Route("Order")]
        public ResponseOrders Order(Orders order)
        {
            Orders orderDetails = OrdersLogic.getInstance().Add(order);
            try
            {
                return new ResponseOrders
                {
                    status = "success",
                    message = "user successfully inserted",
                    Data = order
                };
            }
            catch (Exception e)
            {
                Logging.Log.getInstance().CreateLogError(e, JsonConvert.SerializeObject(order));
                return new ResponseOrders
                {
                    status = "failed",
                    message = e.Message
                };
            }
        }
    }
}

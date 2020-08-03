using BMotionServices.Entity;
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
    [RoutePrefix("api/Profile")]
    public class ProfileController : ApiController
    {
        BMotionDBEntities db = new BMotionDBEntities();
        [HttpPost]
        [Route("Profiles")]
        public ResponsePurchaseHistory Profiles(PurchaseHistory historyItem)
        {
            List<PurchaseHistory> listHistory = new List<PurchaseHistory>();

            try
            {
                var list = db.sp_HistorPerUser(historyItem.NIP).ToList();
                foreach(var item in list)
                {   
                    historyItem = new PurchaseHistory();
                    historyItem.OrderNo = item.OrderNo;
                    historyItem.OutletNo = item.OutletNo;
                    historyItem.TransactionDate = item.TransactionDate;
                    historyItem.Liter = item.Liter;
                    listHistory.Add(historyItem);
                }

                return new ResponsePurchaseHistory
                {
                    status = "success",
                    message = "List Purchase History",
                    Data = listHistory
                };
            }
            catch (Exception e)
            {
                Logging.Log.getInstance().CreateLogError(e, JsonConvert.SerializeObject(listHistory));
                return new ResponsePurchaseHistory
                {
                    status = "failed",
                    message = e.Message
                };
            }
        }
    }
}

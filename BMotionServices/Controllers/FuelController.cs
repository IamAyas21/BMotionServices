using BMotionServices.Entity;
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
    [RoutePrefix("api/Fuel")]
    public class FuelController : ApiController
    {
        BMotionDBEntities db = new BMotionDBEntities();
        [HttpPost]
        [Route("Fuels")]
        public ResponseFuels Fuels(Models.Fuel fuel)
        {
            try
            {
                var fuelList = db.Fuels.Where(f => f.IsSubsidy.Equals(fuel.IsSubsidy)).ToList();
                if (fuelList.Count > 0)
                {
                    List<Models.Fuel> fuelModels = new List<Models.Fuel>();
                    Models.Fuel fuelModel = new Models.Fuel();
                    foreach (var item in fuelList)
                    {
                        fuelModel = new Models.Fuel();
                        fuelModel.FuelId = item.FuilId;
                        fuelModel.Name = item.Name;
                        fuelModel.IsSubsidy = item.IsSubsidy;
                        fuelModel.Price = item.Price.ToString();
                        fuelModel.BackgroundColor = item.BackgroundColor;
                        fuelModel.TextColor = item.TextColor;
                        fuelModels.Add(fuelModel);
                    }


                    return new ResponseFuels
                    {
                        status = "success",
                        message = "fuels found",
                        Data = fuelModels
                    };
                }
                else
                {
                    return new ResponseFuels
                    {
                        status = "failed",
                        message = "fuels not found"
                    };
                }
            }
            catch (Exception e)
            {
                Logging.Log.getInstance().CreateLogError(e, JsonConvert.SerializeObject(fuel));
                return new ResponseFuels
                {
                    status = "failed",
                    message = e.Message
                };
            }
        }
    }
}

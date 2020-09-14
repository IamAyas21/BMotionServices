using BMotionServices.Entity;
using BMotionServices.Parse;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace BMotionServices.Controllers
{

    [RoutePrefix("api/Info")]
    public class InfoController : ApiController
    {
        BMotionDBEntities db = new BMotionDBEntities();
        // GET: Info
        [HttpPost]
        [Route("All")]
        public ResponseInfo All()
        {
            try
            {
                var infoList = db.sp_Notification().ToList();
                if (infoList.Count > 0)
                {
                    return new ResponseInfo
                    {
                        status = "success",
                        message = "info found",
                        Data = infoList
                    };
                }
                else
                {
                    return new ResponseInfo
                    {
                        status = "failed",
                        message = "info not found"
                    };
                }
            }
            catch (Exception e)
            {
                return new ResponseInfo
                {
                    status = "failed",
                    message = e.Message
                };
            }
        }
    }
}
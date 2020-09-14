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
    [RoutePrefix("api/Feedback")]
    public class FeedbackController : ApiController
    {
        BMotionDBEntities db = new BMotionDBEntities();
        [HttpPost]
        [Route("Add")]
        public ResponseFeedback All(FeedbackModels Model)
        {
            try
            {
                var infoList = db.sp_FeedbackInsert(Model.Nip,Model.Message);
                return new ResponseFeedback
                {
                    status = "success",
                    message = "feedback has been inserted."
                };
            }
            catch (Exception e)
            {
                Logging.Log.getInstance().CreateLogError(e, JsonConvert.SerializeObject(Model));
                return new ResponseFeedback
                {
                    status = "failed",
                    message = e.Message
                };
            }
        }
    }
}

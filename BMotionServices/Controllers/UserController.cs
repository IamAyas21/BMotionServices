using BMotionServices.Entity;
using BMotionServices.Logic;
using BMotionServices.Models;
using BMotionServices.Parse;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web;
using System.Web.Http;

namespace BMotionServices.Controllers
{

    //[Authorize]
    [RoutePrefix("api/User")]
    public class UserController : ApiController
    {
        BMotionDBEntities db = new BMotionDBEntities();

        [Route("All")]
        public List<User> GetAllUsers()
        {
            return UserLogic.getInstance().getAllUsers();
        }
        
        [HttpPost]
        [Route("Login")]
        public ResponseUsers Login(Users user)
        {
            try
            {
                var userList = db.Users.Where(usr => usr.Email.Equals(user.Email) && usr.Password.Equals(user.Password)).ToList();
                if (userList.Count > 0)
                {
                    return new ResponseUsers
                    {
                        status = "success",
                        message = "user found",
                        Data = new Users
                        {
                            Email = userList.FirstOrDefault().Email,
                            Name = userList.FirstOrDefault().Name,
                            NIP = userList.FirstOrDefault().NIP,
                            Phone = userList.FirstOrDefault().Phone,
                            Profession = userList.FirstOrDefault().Profession
                        }
                    };
                }
                else
                {
                    return new ResponseUsers
                    {
                        status = "failed",
                        message = "user not found"
                    };
                }
            }
            catch (Exception e)
            {
                Logging.Log.getInstance().CreateLogError(e, JsonConvert.SerializeObject(user));
                return new ResponseUsers
                {
                    status = "failed",
                    message = e.Message
                };
            }
        }


        [HttpPost]
        [Route("Register")]
        public ResponseUsers Register()
        {
            Users user = UserLogic.getInstance().Add();
            try
            {
                if(user.isSuccess)
                {
                    return new ResponseUsers
                    {
                        status = "success",
                        message = "user successfully inserted",
                        Data = new Users
                        {
                            Email = user.Email,
                            Name = user.Name,
                            NIP = user.NIP,
                            Phone = user.Phone,
                            Profession = user.Profession
                        }
                    };
                }
                else
                {
                    return new ResponseUsers
                    {
                        status = "failed",
                        message = "user already",
                    };
                }
            }
            catch (Exception e)
            {
                Logging.Log.getInstance().CreateLogError(e, JsonConvert.SerializeObject(user));
                return new ResponseUsers
                {
                    status = "failed",
                    message = e.Message
                };
            }
        }
    }
}

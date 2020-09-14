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
        private static string pathImageProfile = ConfigurationManager.AppSettings["PathImageProfile"];
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
                    var quota = db.sp_UserQuota(userList.FirstOrDefault().NIP).FirstOrDefault();
                    var purchasedBBM = db.sp_UserPurchasedBBM(userList.FirstOrDefault().NIP).FirstOrDefault();
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
                            Profession = userList.FirstOrDefault().Profession,
                            Quota = quota.Quota,
                            PurchaseBBM = purchasedBBM,
                            Password = userList.FirstOrDefault().Password,
                            Verification = userList.FirstOrDefault().IsVerify,
                            ImageProfilePath = string.Format("{0}/{1}", pathImageProfile, userList.FirstOrDefault().KTP)
                        }
                    };
                }
                else
                {
                    userList = db.Users.Where(usr => usr.Phone.Equals(user.Email) && usr.Password.Equals(user.Password)).ToList();
                    if (userList.Count > 0)
                    {
                        var quota = db.sp_UserQuota(userList.FirstOrDefault().NIP).FirstOrDefault();
                        var purchasedBBM = db.sp_UserPurchasedBBM(userList.FirstOrDefault().NIP).FirstOrDefault();
                        return new ResponseUsers
                        {
                            status = "success",
                            message = "user success login",
                            Data = new Users
                            {
                                Email = userList.FirstOrDefault().Email,
                                Name = userList.FirstOrDefault().Name,
                                NIP = userList.FirstOrDefault().NIP,
                                Phone = userList.FirstOrDefault().Phone,
                                Profession = userList.FirstOrDefault().Profession,
                                Quota = quota.Quota,
                                PurchaseBBM = purchasedBBM,
                                Password = userList.FirstOrDefault().Password,
                                Verification = userList.FirstOrDefault().IsVerify,
                                ImageProfilePath = string.Format("{0}/{1}", pathImageProfile, userList.FirstOrDefault().KTP)
                            }
                        };
                    }
                    else
                    {
                        return new ResponseUsers
                        {
                            status = "failed",
                            message = "user failed login"
                        };
                    }
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
                db = new BMotionDBEntities();
                var quota = db.sp_UserQuota(user.NIP).FirstOrDefault();
                var purchasedBBM = db.sp_UserPurchasedBBM(user.NIP).FirstOrDefault();
                if (user.isSuccess)
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
                            Profession = user.Profession,
                            Quota = quota.Quota == null ? "0 Ltr" : quota.Quota,
                            PurchaseBBM = purchasedBBM == null ? "0 Ltr" : purchasedBBM,
                            Password = user.Password,
                            Verification = "N",
                            ImageProfilePath = string.Format("{0}/{1}", pathImageProfile, user.KTP)
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

        [HttpPost]
        [Route("LimitQuota")]
        public ResponseUsers LimitQuota(Users user)
        {
            try
            {
                var userList = db.Users.Where(usr => usr.NIP.Equals(user.NIP)).ToList();
                if (userList.Count > 0)
                {
                    string purchasedBBM = string.Empty;
                    int limitQuota = 0;
                    var quota = db.sp_UserQuota(userList.FirstOrDefault().NIP).FirstOrDefault();
                    return new ResponseUsers
                    {
                        status = "success",
                        message = "",
                        Data = new Users
                        {
                            Email = userList.FirstOrDefault().Email,
                            Name = userList.FirstOrDefault().Name,
                            NIP = userList.FirstOrDefault().NIP,
                            Phone = userList.FirstOrDefault().Phone,
                            Profession = userList.FirstOrDefault().Profession,
                            Quota = quota.Quota,
                            PurchaseBBM = quota.TotalPurchaseBBM,
                            Password = userList.FirstOrDefault().Password,
                            Verification = userList.FirstOrDefault().IsVerify,
                            ImageProfilePath = string.Format("{0}/{1}", pathImageProfile, userList.FirstOrDefault().KTP),
                }
                    };
                }
                else
                {
                    return new ResponseUsers
                    {
                        status = "failed",
                        message = ""
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

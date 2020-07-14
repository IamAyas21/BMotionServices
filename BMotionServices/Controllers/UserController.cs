using BMotionServices.Entity;
using BMotionServices.Models;
using BMotionServices.Parse;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace BMotionServices.Controllers
{

    //[Authorize]
    [RoutePrefix("api/User")]
    public class UserController : ApiController
    {
        BMotionDBEntities db = new BMotionDBEntities();

        [Route("All")]
        public List<Users> GetAllUsers()
        {
            return UserModels.getInstance().getAllUsers();
        }

        [Route("login")]
        public ResponseUsers Login(Users user)
        {
            var userList = db.Users.Where(usr => usr.NIP.Equals(user.NIP) && usr.Password.Equals(user.Password)).ToList();
            if (userList.Count  > 0)
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
                    message = "user not found",
                    Data = new Users
                    {

                    }
                };
            }
        }
    }
}

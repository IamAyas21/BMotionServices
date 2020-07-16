using BMotionServices.Entity;
using BMotionServices.Models;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace BMotionServices.Logic
{
    public class UserLogic
    {
        BMotionDBEntities db = new BMotionDBEntities();
        List<User> userList;
        static UserLogic usrRgs = null;
        private UserLogic()
        {
            userList = db.Users.ToList();
        }
        public static UserLogic getInstance()
        {
            if (usrRgs == null)
            {
                usrRgs = new UserLogic();
                return usrRgs;
            }
            else
            {
                return usrRgs;
            }
        }
        
        public void Add(Users user)
        {
            try
            {
                User userEntity = new User();
                userEntity.NIP = user.NIP;
                userEntity.Email = user.Email;
                userEntity.Name = user.Name;
                userEntity.Password = user.Password;
                userEntity.Phone = user.Phone;
                userEntity.Profession = user.Profession;
                //userEntity.RoleId = user.RoleId;

                db.Users.Add(userEntity);
                db.SaveChanges();
            }
            catch (Exception e)
            {
                Logging.Log.getInstance().CreateLogError(e, JsonConvert.SerializeObject(user));
            }
        }
       
        public List<User> getAllUsers()
        {
            return userList;
        }
       
    }
}
using BMotionServices.Entity;
using BMotionServices.Models;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.IO;
using System.Linq;
using System.Web;

namespace BMotionServices.Logic
{
    public class UserLogic
    {
        private static string pathUpload = ConfigurationManager.AppSettings["PathUploads"];
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
        
        public Users Add()
        {
            Users user = new Users();
            user.isSuccess = false;
            try
            {
                HttpContext postedContext = HttpContext.Current;
                HttpFileCollection Request = postedContext.Request.Files;

                user.NIP = postedContext.Request.Params["nip"];
                user.Name = postedContext.Request.Params["name"];
                user.Password = postedContext.Request.Params["password"];
                user.Phone = postedContext.Request.Params["phone"];
                user.Profession = postedContext.Request.Params["profession"];
                user.Email = postedContext.Request.Params["email"];
                user.KTP = postedContext.Request.Params["ktp"];

                var userList = db.Users.Where(usr => usr.Email.Equals(user.Email) || usr.NIP.Equals(user.NIP)).ToList();
                if (userList.Count == 0)
                {
                    if (Request.Count != 0)
                    {
                        if (Request["imagektp"].ContentType.ToLower() == "image/jpg" ||
                        Request["imagektp"].ContentType.ToLower() == "image/jpeg" ||
                        Request["imagektp"].ContentType.ToLower() == "image/png")
                        {
                            HttpPostedFile file = Request[0];

                            string fileName = file.FileName;
                            var path = Path.Combine(
                                HttpContext.Current.Server.MapPath(pathUpload),
                                DateTime.Now.ToString("ddMMyyyy")
                            );

                            if (!Directory.Exists(path))
                            {
                                // This path is a directory
                                System.IO.Directory.CreateDirectory(path);
                            }

                            using (var fileStream = new System.IO.FileStream(path + "\\" + fileName, System.IO.FileMode.Create, System.IO.FileAccess.Write))
                            {
                                file.InputStream.CopyTo(fileStream);
                            }

                            db = new BMotionDBEntities();
                            User userEntity = new User();
                            userEntity.NIP = user.NIP.ToString().Replace('"', ' ').Trim();
                            userEntity.Email = user.Email.ToString().Replace('"', ' ').Replace('"', ' ').Trim();
                            userEntity.Name = user.Name.ToString().Replace('"', ' ').Replace('"', ' ').Trim();
                            //userEntity.Password = user.Password.ToString().Replace('"', ' ').Replace('"', ' ');
                            userEntity.Phone = user.Phone.ToString().Replace('"', ' ').Replace('"', ' ').Trim();
                            //userEntity.Profession = user.Profession.ToString().Replace('"', ' ').Replace('"', ' ');
                            userEntity.KTP = user.KTP.ToString().Replace('"', ' ').Replace('"', ' ').Trim();
                            //userEntity.verification = user.Verification.ToString().Replace('"', ' ').Replace('"', ' ');
                            //userEntity.RoleId = user.RoleId;

                            db.Users.Add(userEntity);
                            db.SaveChanges();
                        }
                    }

                    user.isSuccess = true;
                }
                
                return user;
            }
            catch (Exception e)
            {
                Logging.Log.getInstance().CreateLogError(e, JsonConvert.SerializeObject(user));
                throw e;
            }
        }
       
        public List<User> getAllUsers()
        {
            return userList;
        }
       
    }
}
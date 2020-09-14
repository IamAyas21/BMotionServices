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
                user.District = postedContext.Request.Params["city"];

                var userList = db.Users.Where(usr => usr.Email.Equals(user.Email) || usr.NIP.Equals(user.NIP)).ToList();
                if (userList.Count == 0)
                {
                    if (Request.Count != 0)
                    {
                        if (Request["imagektp"].ContentType.ToLower() == "image/jpg" ||
                        Request["imagektp"].ContentType.ToLower() == "image/jpeg" ||
                        Request["imagektp"].ContentType.ToLower() == "image/png" ||
                        Request["filepdf"].ContentType.ToLower() == "application/pdf")
                        {
                            HttpPostedFile imgKtp = Request["imagektp"];

                            user.ImageKTP = imgKtp.FileName;
                            var pathImgKtp = Path.Combine(pathUpload, "KTP");//(HttpContext.Current.Server.MapPath(pathUpload), dateDayNow,"KTP");
                            if (!Directory.Exists(pathImgKtp))
                            {
                                System.IO.Directory.CreateDirectory(pathImgKtp);
                            }
                            using (var fileStream = new System.IO.FileStream(pathImgKtp + "\\" + user.ImageKTP, System.IO.FileMode.Create, System.IO.FileAccess.Write))
                            {
                                imgKtp.InputStream.CopyTo(fileStream);
                            }

                            string strUser = user.NIP.ToString().Replace('"', ' ').Replace('\\', ' ').Trim();
                            db = new BMotionDBEntities();
                            User userEntity = new User();
                            userEntity.NIP = strUser;
                            userEntity.Email = user.Email.ToString().Replace('"', ' ').Replace('\\', ' ').Trim();
                            userEntity.Name = user.Name.ToString().Replace('"', ' ').Replace('\\', ' ').Trim();
                            userEntity.Phone = user.Phone.ToString().Replace('"', ' ').Replace('\\', ' ').Trim();
                            userEntity.KTP = user.KTP.ToString().Replace('"', ' ').Replace('\\', ' ').Trim();
                            userEntity.Password = user.Password.ToString().Replace('"', ' ').Replace('\\', ' ');
                            userEntity.CreatedDate = DateTime.Now;
                            userEntity.CreatedBy = strUser;
                            userEntity.Password = user.Password.ToString().Replace('"', ' ').Replace('\\', ' ').Trim();
                            userEntity.IsVerify = "N";
                            userEntity.District = user.District;
                            //userEntity.verification = user.Verification.ToString().Replace('"', ' ').Replace('"', ' ');
                            //userEntity.Profession = user.Profession.ToString().Replace('"', ' ').Replace('"', ' ');
                            //userEntity.RoleId = user.RoleId;
                            db.Users.Add(userEntity);
                            db.SaveChanges();

                            HttpPostedFile filePdf = Request["filepdf"];
                            if (filePdf != null)
                            {
                                user.ExpDate = postedContext.Request.Params["expdate"];
                                user.Quota = postedContext.Request.Params["quota"];
                                user.DocumentNo = postedContext.Request.Params["documentNo"];

                                user.FilePDF = filePdf.FileName;

                                var pathFilePdf = Path.Combine(pathUpload, "Document");//(HttpContext.Current.Server.MapPath(pathUpload),dateDayNow, "Document");

                                if (!Directory.Exists(pathFilePdf))
                                {
                                    System.IO.Directory.CreateDirectory(pathFilePdf);
                                }

                                using (var fileStream = new System.IO.FileStream(pathFilePdf + "\\" + user.FilePDF, System.IO.FileMode.Create, System.IO.FileAccess.Write))
                                {
                                    filePdf.InputStream.CopyTo(fileStream);
                                }

                                db = new BMotionDBEntities();
                                Document docEntity = new Document();
                                docEntity.DocumentNo = user.DocumentNo;
                                docEntity.NIP = strUser;
                                docEntity.Quota = Convert.ToInt32(user.Quota.ToString().Replace('"', ' ').Replace('\\', ' ').Trim());
                                docEntity.DocumentFile = user.FilePDF;
                                docEntity.ExpDate = Convert.ToDateTime(user.ExpDate.ToString().Replace('"', ' ').Replace('\\', ' ').Trim());
                                docEntity.CreatedDate = DateTime.Now;
                                docEntity.CreatedBy = strUser;
                                docEntity.IsVerify = "N";
                                db.Documents.Add(docEntity);
                                db.SaveChanges();
                            }
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
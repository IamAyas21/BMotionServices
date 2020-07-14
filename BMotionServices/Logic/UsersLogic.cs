using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace BMotionServices.Models
{
    public class UserModels
    {
        List<Users> userList;
        static UserModels usrRgs = null;
        private UserModels()
        {
            userList = new List<Users>();
        }
        public static UserModels getInstance()
        {
            if (usrRgs == null)
            {
                usrRgs = new UserModels();
                return usrRgs;
            }
            else
            {
                return usrRgs;
            }
        }
        public void Add(Users users)
        {
            userList.Add(users);
        }
        public String Remove(String nip)
        {
            for (int i = 0; i < userList.Count; i++)
            {
                Users usrs = userList.ElementAt(i);
                if (usrs.NIP.Equals(nip))
                {
                    userList.RemoveAt(i);//update the new record
                    return "Delete successful";
                }
            }
            return "Delete un-successful";
        }
        public List<Users> getAllUsers()
        {
            return userList;
        }
        public String UpdateUsers(Users users)
        {
            for (int i = 0; i < userList.Count; i++)
            {
                Users usrs = userList.ElementAt(i);
                if (usrs.NIP.Equals(users.NIP))
                {
                    userList[i] = users;//update the new record
                    return "Update successful";
                }
            }
            return "Update un-successful";
        }
    }
}
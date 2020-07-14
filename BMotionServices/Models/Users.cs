using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace BMotionServices.Models
{
    public class Users
    {
        string nip;
        public String NIP
        {
            get { return nip; }
            set { nip = value; }
        }

        int roleId;
        public int RoleId
        {
            get { return roleId; }
            set { roleId = value; }
        }

        string name;
        public String Name
        {
            get { return name; }
            set { name = value; }
        }

        string profession;
        public String Profession
        {
            get { return profession; }
            set { profession = value; }
        }


        string email;
        public String Email
        {
            get { return email; }
            set { email = value; }
        }

        string password;
        public String Password
        {
            get { return password; }
            set { password = value; }
        }

        string phone;
        public String Phone
        {
            get { return phone; }
            set { phone = value; }
        }
    }
}
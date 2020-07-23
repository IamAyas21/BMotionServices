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

        string ktp;
        public String KTP
        {
            get { return ktp; }
            set { ktp = value; }
        }

        string verification;
        public String Verification
        {
            get { return verification; }
            set { verification = value; }
        }

        string imagektp;
        public String ImageKTP
        {
            get { return imagektp; }
            set { imagektp = value; }
        }

        string filepdf;
        public String FilePDF
        {
            get { return filepdf; }
            set { filepdf = value; }
        }

        String expdate;
        public String ExpDate
        {
            get { return expdate; }
            set { expdate = value; }
        }

        bool issuccess;
        public bool isSuccess
        {
            get { return issuccess; }
            set { issuccess = value; }
        }

    }
}
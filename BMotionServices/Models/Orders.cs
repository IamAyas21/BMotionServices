using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace BMotionServices.Models
{
    public class Orders
    {
        string orderNo;
        public String OrderNo
        {
            get { return orderNo; }
            set { orderNo = value; }
        }

        string nip;
        public String NIP
        {
            get { return nip; }
            set { nip = value; }
        }

        string isVerify;
        public String IsVerify
        {
            get { return isVerify; }
            set { isVerify = value; }
        }

        string createdDate;
        public String CreatedDate
        {
            get { return createdDate; }
            set { createdDate = value; }
        }

        string expiredDate;
        public String ExpiredDate
        {
            get { return expiredDate; }
            set { expiredDate = value; }
        }

        List<OrderDetails> orderDetails;
        public List<OrderDetails> OrderDetails
        {
            get { return orderDetails; }
            set { orderDetails = value; }
        }
    }
}
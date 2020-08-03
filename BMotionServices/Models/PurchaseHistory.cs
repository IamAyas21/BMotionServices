using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace BMotionServices.Models
{
    public class PurchaseHistory
    {
        string nip;
        public String NIP
        {
            get { return nip; }
            set { nip = value; }
        }

        string orderNo;
        public String OrderNo
        {
            get { return orderNo; }
            set { orderNo = value; }
        }
        string transactionDate;
        public String TransactionDate
        {
            get { return transactionDate; }
            set { transactionDate = value; }
        }

        string outletNo;
        public String OutletNo
        {
            get { return outletNo; }
            set { outletNo = value; }
        }

        string liter;
        public String Liter
        {
            get { return liter; }
            set { liter = value; }
        }
    }
}
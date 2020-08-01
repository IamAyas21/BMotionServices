using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace BMotionServices.Models
{
    public class OrderDetails
    {
        int orderDetailId;
        public int OrderDetailId
        {
            get { return orderDetailId; }
            set { orderDetailId = value; }
        }

        int fuelId;
        public int FuelId
        {
            get { return fuelId; }
            set { fuelId = value; }
        }

        string orderNo;
        public String OrderNo
        {
            get { return orderNo; }
            set { orderNo = value; }
        }

        int liter;
        public int Liter
        {
            get { return liter; }
            set { liter = value; }
        }
    }
}
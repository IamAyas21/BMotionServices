using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace BMotionServices.Models
{
    public class Fuel
    {
        int fuelId;
        public int FuelId
        {
            get { return fuelId; }
            set { fuelId = value; }
        }

        string name;
        public String Name
        {
            get { return name; }
            set { name = value; }
        }

        string price;
        public String Price
        {
            get { return price; }
            set { price = value; }
        }

        string isSubsidy;
        public String IsSubsidy
        {
            get { return isSubsidy; }
            set { isSubsidy = value; }
        }

        string backgroundColor;
        public String BackgroundColor
        {
            get { return backgroundColor; }
            set { backgroundColor = value; }
        }

        string textColor;
        public String TextColor
        {
            get { return textColor; }
            set { textColor = value; }
        }
    }
}
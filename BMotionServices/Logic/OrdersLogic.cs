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
    public class OrdersLogic
    {
        BMotionDBEntities db = new BMotionDBEntities();
        
        static OrdersLogic orderLgc = null;
        private OrdersLogic()
        {
           
        }
        public static OrdersLogic getInstance()
        {
            if (orderLgc == null)
            {
                orderLgc = new OrdersLogic();
                return orderLgc;
            }
            else
            {
                return orderLgc;
            }
        }
        
        public Orders Add(Orders order)
        {
            try
            {
                Guid guidId = Guid.NewGuid();
                db = new BMotionDBEntities();
                Order orderEntity = new Order();
                orderEntity.OrderNo = guidId.ToString();
                orderEntity.NIP = order.NIP;
                orderEntity.IsVerify = "N";
                orderEntity.CreatedDate = DateTime.Now;
                orderEntity.CreatedBy = orderEntity.NIP;
                orderEntity.ExpiredDate = DateTime.Now.AddHours(4);
                order.ExpiredDate = DateTime.Now.AddHours(4).ToString("HH:mm");
                db.Orders.Add(orderEntity);
                db.SaveChanges();

                foreach (var item in order.OrderDetails)
                {
                    db = new BMotionDBEntities();

                    var fPrice = from f in db.Fuels
                                 where f.FuilId == item.FuelId
                                 select f.Price;
                    decimal price = Convert.ToDecimal(fPrice.FirstOrDefault().ToString());

                    OrderDetail orderDetailEntity = new OrderDetail();
                    orderDetailEntity.OrderNo = orderEntity.OrderNo;
                    orderDetailEntity.FuelId = item.FuelId;
                    orderDetailEntity.Liter = item.Liter;
                    orderDetailEntity.Price = price;
                    orderDetailEntity.CreatedDate = DateTime.Now;
                    orderDetailEntity.CreatedBy = orderEntity.NIP;
                    db.OrderDetails.Add(orderDetailEntity);
                    db.SaveChanges();
                }

                order.OrderNo = guidId.ToString();
                order.IsVerify = "N";

                return order;
            }
            catch (Exception e)
            {
                Logging.Log.getInstance().CreateLogError(e, JsonConvert.SerializeObject(order));
                throw e;
            }
        }       
    }
}
import { useState } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import {
  checkoutSchema,
  type CheckoutFormData,
} from "../schemas/checkoutSchema";
import { useCartStore } from "../store/cartStore";
import {
  createOrder,
  createPayment,
  verifyPayment,
} from "../api/paymentApi";

declare global {
  interface Window {
    Razorpay: any;
  }
}

function CheckoutForm() {
  const getTotal = useCartStore((state) => state.getTotal);
  const cartItems = useCartStore((state) => state.cartItems);
  const clearCart = useCartStore((state) => state.clearCart);

  const total = getTotal();

  const [message, setMessage] = useState("");

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<CheckoutFormData>({
    resolver: zodResolver(checkoutSchema),
  });

  const onSubmit = async (data: CheckoutFormData) => {
    try {
      setMessage("Creating order...");

      const items = cartItems.map((item) => ({
        productId: item.product.productId,
        quantity: item.quantity,
    }));

      const order = await createOrder(items);

      setMessage("Opening payment...");

      const payment = await createPayment(order.orderId);

      const options = {
        key: payment.keyId,
        amount: payment.amount,
        currency: payment.currency,
        name: "SmartCart",
        description: "SmartCart Order",
        order_id: payment.razorpayOrderId,

        prefill: {
          name: data.name,
          email: data.email,
          contact: data.phone,
        },

        handler: async (response: any) => {
          try {
            await verifyPayment({
              orderId: order.orderId,
              razorpayOrderId: response.razorpay_order_id,
              razorpayPaymentId: response.razorpay_payment_id,
              razorpaySignature: response.razorpay_signature,
            });

            clearCart();

            setMessage(
              "Payment successful! Order placed successfully."
            );
          } catch {
            setMessage("Payment verification failed.");
          }
        },

        theme: {
          color: "#3399cc",
        },
      };

      const razorpay = new window.Razorpay(options);

      razorpay.open();
    } catch {
      setMessage("Unable to create order or payment.");
    }
  };

  return (
    <div className="checkout">
      <h2>Checkout</h2>

      <form onSubmit={handleSubmit(onSubmit)}>

        <div>
          <label>Name</label>

          <input
            type="text"
            {...register("name")}
          />

          {errors.name && (
            <p className="error-message">
              {errors.name.message}
            </p>
          )}
        </div>

        <div>
          <label>Email</label>

          <input
            type="email"
            {...register("email")}
          />

          {errors.email && (
            <p className="error-message">
              {errors.email.message}
            </p>
          )}
        </div>

        <div>
          <label>Phone</label>

          <input
            type="tel"
            {...register("phone")}
          />

          {errors.phone && (
            <p className="error-message">
              {errors.phone.message}
            </p>
          )}
        </div>

        <div>
          <label>Address</label>

          <textarea
            {...register("address")}
          />

          {errors.address && (
            <p className="error-message">
              {errors.address.message}
            </p>
          )}
        </div>

        <h3>
          Order Total: ₹{total}
        </h3>

        <button type="submit">
          Pay ₹{total}
        </button>

        {message && (
          <p className="form-message">
            {message}
          </p>
        )}

      </form>
    </div>
  );
}

export default CheckoutForm;
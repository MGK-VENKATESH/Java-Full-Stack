import axios from "axios";

const API_URL = `${import.meta.env.VITE_API_URL}/api/payment`;
const ORDER_API_URL = `${import.meta.env.VITE_API_URL}/api/orders`;

const getAuthHeaders = () => ({
  headers: {
    Authorization: `Bearer ${localStorage.getItem("token")}`,
  },
});

export const createOrder = async (
  items: {
    productId: number;
    quantity: number;
  }[]
) => {
  const response = await axios.post(
    ORDER_API_URL,
    { items },
    getAuthHeaders()
  );

  return response.data;
};

export const createPayment = async (orderId: number) => {
  const response = await axios.post(
    `${API_URL}/create/${orderId}`,
    {},
    getAuthHeaders()
  );

  return response.data;
};

export const verifyPayment = async (data: {
  orderId: number;
  razorpayOrderId: string;
  razorpayPaymentId: string;
  razorpaySignature: string;
}) => {
  const response = await axios.post(
    `${API_URL}/verify`,
    data,
    getAuthHeaders()
  );

  return response.data;
};
import { Client } from "@stomp/stompjs";
import { useEffect, useMemo } from "react";
import SockJS from "sockjs-client";

const WEBSOCKET_URL = "http://localhost:8080/ws";

export const useWebsocket = (url = WEBSOCKET_URL) => {
  const client = useMemo(
    () =>
      new Client({
        webSocketFactory: () => new SockJS(url),
        connectHeaders: {
          login: localStorage.getItem("token"),
        },
        onDisconnect: () => {
          console.log("Disconnected from the broker");
        },
      }),
    [url]
  );

  // Sử dụng useEffect để kích hoạt kết nối khi component mount và ngắt kết nối khi unmount
  useEffect(() => {
    client.activate(); // Kích hoạt kết nối WebSocket

    return () => {
      client.deactivate(); // Ngắt kết nối khi component unmount
    };
  }, [client]);

  return client;
};

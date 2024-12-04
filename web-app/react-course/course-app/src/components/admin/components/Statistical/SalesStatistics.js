import React, { useEffect, useState } from "react";
import { Line } from "react-chartjs-2";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
} from "chart.js";
import { getRevenueData } from "../../service/SalesStatisticsService"; // API service
import { Select, Spin } from "antd"; // Import Select từ Ant Design
import { color } from "framer-motion";

ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend
);

const SalesStatistics = () => {
  const [data, setData] = useState({ labels: [], datasets: [] });
  const [year, setYear] = useState(2024); // Năm mặc định
  const [loading, setLoading] = useState(false);

  const years = [2024, 2025, 2026]; // Các năm có thể chọn

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true);
      try {
        const result = await getRevenueData(year);
        const months = result.map((item) => item.month);
        const totalRevenue = result.map((item) => item.totalRevenue);
        const totalCoursesSold = result.map((item) => item.totalCoursesSold);

        setData({
          labels: months.map((month) => `Tháng ${month}`),
          datasets: [
            {
              label: "Number of courses",
              data: totalCoursesSold,
              borderColor: "green",
              backgroundColor: "rgba(0, 128, 0, 0.2)",
              fill: true,
              yAxisID: "y1",
            },
            {
              label: "Revenue",
              data: totalRevenue,
              borderColor: "red",
              backgroundColor: "rgba(255, 99, 132, 0.2)",
              fill: true,
              yAxisID: "y2",
            },
          ],
        });
      } catch (error) {
        console.error("Error fetching revenue data:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [year]);

  const handleYearChange = (value) => {
    setYear(value);
  };

  // Cấu hình biểu đồ
  const chartOptions = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      tooltip: {
        backgroundColor: "#333",
        titleColor: "white",
        bodyColor: "white",
      },
      legend: {
        labels: {
          color: "white",
          font: {
            size: 14,
            family: "'Arial', sans-serif",
            color: "white",
          },
        },
      },
    },
    scales: {
      x: {
        ticks: {
          color: "white",
          font: {
            size: 12,
            family: "'Arial', sans-serif",
            color: "white",
          },
        },
      },
      y1: {
        ticks: {
          color: "white",
          font: {
            size: 12,
            family: "'Arial', sans-serif",
            color: "white",
          },
          beginAtZero: true,
        },
        position: "left",
        title: {
          color: "white",
          display: true,
          text: "Number of courses",
          font: {
            size: 14,
            family: "'Arial', sans-serif",
            color: "white",
          },
        },
      },
      y2: {
        ticks: {
          color: "white",
          font: {
            size: 12,
            family: "'Arial', sans-serif",
            color: "white",
          },
          beginAtZero: true,
        },
        position: "right",
        title: {
          color: "white",
          display: true,
          text: "Revenue",
          font: {
            size: 14,
            family: "'Arial', sans-serif",
            color: "white",
          },
        },
      },
    },
  };

  return (
    <div
      style={{ padding: "20px", backgroundColor: "#333", borderRadius: "10px" }}
    >
      <h2 style={{ color: "white", textAlign: "center" }}>
        Revenue and Number of Courses Sold in {year}
      </h2>
      <Select
        value={year}
        onChange={handleYearChange}
        style={{
          width: 120,
          marginBottom: "20px",
          display: "block",
          marginLeft: "auto",
          marginRight: "auto",
        }}
        dropdownStyle={{ backgroundColor: "#333", color: "white" }}
      >
        {years.map((yearOption) => (
          <Select.Option
            key={yearOption}
            value={yearOption}
            style={{ color: "white" }}
          >
            {yearOption}
          </Select.Option>
        ))}
      </Select>

      {loading ? (
        <div style={{ textAlign: "center" }}>
          <Spin size="large" />
        </div>
      ) : (
        <div style={{ height: "400px" }}>
          <Line data={data} options={chartOptions} />
        </div>
      )}
    </div>
  );
};

export default SalesStatistics;

import React, { useState } from "react";
import confetti from "canvas-confetti"; // Sử dụng thư viện confetti để tạo hiệu ứng pháo hoa

// Mảng chứa các câu nói tích cực và cấp độ tương ứng
const motivationalQuotes = [
  // Very Unlucky
  {
    text: "Today you will work, but you will receive a breakup email from your partner!",
    level: "very-unlucky",
  },
  {
    text: "Your entire workday will pass with continuous computer errors!",
    level: "very-unlucky",
  },
  {
    text: "Today will be a terrible day when you forget both your phone and laptop at home!",
    level: "very-unlucky",
  },
  {
    text: "You will have a challenging day, and the peak of it will be your boss calling you to an emergency meeting!",
    level: "very-unlucky",
  },
  {
    text: "Today’s workday is terrible because your Wi-Fi has been down all day!",
    level: "very-unlucky",
  },
  {
    text: "Don’t be surprised if you receive an ‘Urgent’ email from a client while you're having lunch!",
    level: "very-unlucky",
  },
  {
    text: "Today you will work hard, but no one will acknowledge your efforts!",
    level: "very-unlucky",
  },
  {
    text: "You’ll have a dark day when all your computer data gets lost!",
    level: "very-unlucky",
  },
  {
    text: "Don’t expect today to be successful unless you find your lost laptop!",
    level: "very-unlucky",
  },
  {
    text: "You will work all day without completing any important tasks!",
    level: "very-unlucky",
  },

  // Unlucky
  {
    text: "Today your work will be pretty normal, except that you forget an important deadline!",
    level: "unlucky",
  },
  {
    text: "Today will be okay, but don’t be surprised if you get a really annoying call!",
    level: "unlucky",
  },
  {
    text: "You will have a normal workday, except for when you drop your phone on the floor!",
    level: "unlucky",
  },
  {
    text: "Everything will be fine, but don’t forget to check your email or you might face an unexpected issue!",
    level: "unlucky",
  },
  {
    text: "Your workday will go smoothly, but don’t forget to leave important documents on your desk!",
    level: "unlucky",
  },
  {
    text: "Today your work will be okay, but don’t forget to turn off your alarm or you’ll be late for the meeting!",
    level: "unlucky",
  },
  {
    text: "Your day will be pretty easy, but don’t let your computer freeze right before a meeting!",
    level: "unlucky",
  },
  {
    text: "You will work comfortably, but make sure you don’t lose your phone in the car!",
    level: "unlucky",
  },
  {
    text: "Today will be okay, except for the never-ending meeting that prevents you from doing anything else!",
    level: "unlucky",
  },
  {
    text: "Your work will go smoothly, but don’t be surprised if you need to solve an unexpected issue!",
    level: "unlucky",
  },

  // Lucky
  {
    text: "Today you will work efficiently, but don’t forget to check the printer to avoid any surprises!",
    level: "lucky",
  },
  {
    text: "Today will be great, as long as you don’t forget your important documents!",
    level: "lucky",
  },
  {
    text: "Your work today will go easily, but don’t forget to check your laptop before you leave!",
    level: "lucky",
  },
  {
    text: "Today will be a very successful day, but don’t let an email from your boss stress you out!",
    level: "lucky",
  },
  {
    text: "You will finish your tasks quickly, but make sure you don’t face any Wi-Fi issues!",
    level: "lucky",
  },
  {
    text: "Today’s work will go smoothly, except for a few unwanted calls!",
    level: "lucky",
  },
  {
    text: "Everything will go pretty well, but don’t forget to review that important report before sending it to the boss!",
    level: "lucky",
  },
  {
    text: "Today will go easily, but make sure to check your work schedule!",
    level: "lucky",
  },
  {
    text: "You’ll have a productive workday, but don’t forget to prepare for any surprise meetings!",
    level: "lucky",
  },
  {
    text: "Today you’ll be productive, but don’t let unnecessary emails waste your time!",
    level: "lucky",
  },

  // Very Lucky
  {
    text: "Today your work will be amazing, and you will complete all your projects on time!",
    level: "very-lucky",
  },
  {
    text: "Today will be absolutely wonderful, your work will be as easy as a walk in the park!",
    level: "very-lucky",
  },
  {
    text: "Your work today will be great, and all client emails will be full of praise!",
    level: "very-lucky",
  },
  {
    text: "Today will be an exceptionally lucky workday, with all your meetings running smoothly!",
    level: "very-lucky",
  },
  {
    text: "Today will be excellent, and you’ll receive positive feedback from everyone!",
    level: "very-lucky",
  },
  {
    text: "Your work will go smoothly, and your boss will be very pleased with your results!",
    level: "very-lucky",
  },
  {
    text: "Today your work will finish early, and you’ll have extra time to relax!",
    level: "very-lucky",
  },
  {
    text: "Today will bring lots of fantastic opportunities, and all your plans will succeed!",
    level: "very-lucky",
  },
  {
    text: "You will have a great workday, with everything going exactly as you expect!",
    level: "very-lucky",
  },
  {
    text: "Today will go perfectly, you’ll complete your work excellently and receive recognition from colleagues!",
    level: "very-lucky",
  },
];

const Dashboard = () => {
  const [currentQuote, setCurrentQuote] = useState(""); // Câu hiện tại
  const [quoteLevel, setQuoteLevel] = useState(""); // Cấp độ của câu hiện tại
  const [showEffect, setShowEffect] = useState(false); // Biến điều khiển hiệu ứng

  // Hàm chọn ngẫu nhiên câu nói tích cực và cấp độ của nó
  const getRandomQuote = () => {
    const randomIndex = Math.floor(Math.random() * motivationalQuotes.length);
    const randomQuote = motivationalQuotes[randomIndex];
    setCurrentQuote(randomQuote.text);
    setQuoteLevel(randomQuote.level);
    setShowEffect(true);

    // Nếu câu có cấp độ may mắn, hiển thị pháo hoa
    if (randomQuote.level === "very-lucky" || randomQuote.level === "lucky") {
      createConfetti(); // Tạo hiệu ứng pháo hoa
    } else {
      setTimeout(() => setShowEffect(false), 2000); // Tắt hiệu ứng buồn sau 2 giây
    }
  };

  // Hàm tạo hiệu ứng pháo hoa
  const createConfetti = () => {
    confetti({
      particleCount: 100,
      spread: 70,
      origin: { y: 0.6 },
    });
  };

  // Chọn màu sắc và biểu tượng dựa trên cấp độ
  const getStylesForLevel = (level) => {
    switch (level) {
      case "very-unlucky":
        return {
          color: "#e74c3c", // Màu đỏ cho xui xẻo
          icon: "😞", // Biểu tượng mặt buồn
          background: "#f2dede", // Nền xui xẻo màu nhạt
          effect: "sadEffect", // Lớp hiệu ứng buồn
        };
      case "average":
        return {
          color: "#f39c12", // Màu vàng cho trung bình
          icon: "😐", // Biểu tượng mặt không cảm xúc
          background: "#fef7e1", // Nền màu sáng
          effect: "", // Không có hiệu ứng
        };
      case "lucky":
        return {
          color: "#2ecc71", // Màu xanh lá cho may mắn
          icon: "😊", // Biểu tượng mặt cười
          background: "#eafaf1", // Nền màu sáng cho may mắn
          effect: "luckyEffect", // Lớp hiệu ứng may mắn
        };
      case "very-lucky":
        return {
          color: "#3498db", // Màu xanh dương cho rất may mắn
          icon: "😁", // Biểu tượng mặt cười to
          background: "#e1f5fe", // Nền màu rất sáng
          effect: "luckyEffect", // Lớp hiệu ứng may mắn
        };
      default:
        return {
          color: "#ecf0f1", // Màu mặc định (sáng)
          icon: "🌟", // Biểu tượng sao (nếu chưa xác định cấp độ)
          background: "#34495e", // Nền tối
          effect: "", // Không có hiệu ứng
        };
    }
  };

  const { color, icon, background, effect } = getStylesForLevel(quoteLevel);

  return (
    <div
      style={{
        ...styles.dashboard,
        backgroundColor: background, // Đổi nền theo cấp độ
        transition: "background-color 0.5s ease", // Hiệu ứng chuyển nền
      }}
    >
      <h1 style={styles.heading}>Welcome to a new work day!</h1>

      {/* Khung chữ tích cực */}
      {currentQuote && (
        <div
          style={{
            ...styles.quoteBox,
            borderColor: color,
            borderWidth: "5px", // Độ dày viền
          }}
        >
          <p
            style={{
              ...styles.motivationText,
              color: color,
              fontSize: "2rem",
            }}
          >
            {currentQuote.split(" ").map((word, wordIndex) => (
              <span
                key={wordIndex}
                style={{
                  ...styles.motivationWord,
                  animationDelay: `${wordIndex * 0.1}s`,
                }}
              >
                {word.split("").map((char, index) => (
                  <span
                    key={index}
                    style={{
                      ...styles.motivationChar,
                      animationDelay: `${index * 0.1}s`,
                    }}
                  >
                    {char}
                  </span>
                ))}
                {/* Thêm khoảng cách giữa các từ */}
                {wordIndex < currentQuote.split(" ").length - 1 && (
                  <span style={styles.space}></span>
                )}
              </span>
            ))}
            {/* Biểu tượng phía sau dòng chữ */}
            <span style={styles.icon}>{icon}</span>
          </p>
        </div>
      )}

      {/* Nút để lấy câu tích cực ngẫu nhiên */}
      <button onClick={getRandomQuote} style={styles.button}>
        Click to see how your day is going{" "}
        <span style={styles.smileIcon}>{icon}</span>
      </button>

      {/* Hiệu ứng bắn pháo hoa */}
      {showEffect && effect === "luckyEffect" && (
        <div style={styles.effectContainer}>
          <div className="confetti-effect"></div>
        </div>
      )}
      {showEffect && effect === "sadEffect" && (
        <div style={styles.effectContainer}>
          <div className="sad-effect"></div>
        </div>
      )}
    </div>
  );
};

// CSS inline cho component
const styles = {
  dashboard: {
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
    justifyContent: "center",
    height: "100vh",
    backgroundColor: "#34495e", // Nền tối để nổi bật phần tử
    textAlign: "center",
    fontFamily: "'Poppins', sans-serif", // Font chữ đẹp, hiện đại
    padding: "20px",
    boxSizing: "border-box",
    overflow: "hidden", // Đảm bảo các phần tử không bị tràn ra ngoài màn hình
    position: "relative", // Cần thiết để hiệu ứng bùng nổ hiển thị đúng vị trí
  },
  heading: {
    fontSize: "2.8rem", // Lớn hơn một chút để nổi bật
    color: "#ecf0f1", // Màu sáng cho chữ tiêu đề
    marginBottom: "30px",
    fontWeight: "700",
    textShadow: "3px 3px 7px rgba(0, 0, 0, 0.5)", // Bóng đổ mềm mại, đậm hơn
    letterSpacing: "0.05rem", // Thêm khoảng cách giữa các chữ cái
  },
  motivationText: {
    fontSize: "2rem",
    fontWeight: "500",
    letterSpacing: "0.1rem", // Khoảng cách giữa các ký tự
    wordBreak: "break-word", // Tránh văn bản bị tràn ra ngoài
    position: "relative",
  },
  motivationWord: {
    display: "inline-block",
    padding: "5px 10px",
    fontSize: "2.2rem",
    transition: "transform 0.4s ease-in-out",
    animation: "wordAnimation 2s forwards",
  },
  motivationChar: {
    display: "inline-block",
    transition: "transform 0.3s ease-in-out",
  },
  space: {
    display: "inline-block",
  },
  smileIcon: {
    fontSize: "1.5rem",
    paddingLeft: "10px",
  },
  button: {
    backgroundColor: "#2ecc71",
    color: "#fff",
    padding: "15px 25px",
    border: "none",
    borderRadius: "5px",
    cursor: "pointer",
    fontSize: "1.2rem",
    fontWeight: "600",
    boxShadow: "3px 3px 10px rgba(0, 0, 0, 0.3)",
    display: "flex",
    alignItems: "center",
    marginTop: "20px",
    transition: "background-color 0.2s ease",
  },
  effectContainer: {
    position: "absolute",
    top: "50%",
    left: "50%",
    transform: "translate(-50%, -50%)",
  },
  icon: {
    fontSize: "2rem", // Kích thước biểu tượng lớn hơn
    marginLeft: "15px",
  },
};

export default Dashboard;

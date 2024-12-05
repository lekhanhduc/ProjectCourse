import { FaStar } from "react-icons/fa";
import { GiTakeMyMoney, GiTrophyCup } from "react-icons/gi";
import { PiStudentBold } from "react-icons/pi";

const StatisticsOverview = ({ courseOverview }) => {
    return (
        <div className="manager-courses-stats">
            <div className="stats-card">
                <h3 className="icons-lesson-detail-student"><PiStudentBold /></h3>
                <p>{courseOverview.student}</p>
            </div>
            <div className="stats-card">
                <h3 className="icons-lesson-detail-lessons"><FaStar /></h3>
                <p>{courseOverview.avgRating}</p>
            </div>
            <div className="stats-card">
                <h3 className="icons-lesson-detail-chapters"><GiTrophyCup /></h3>
                <p>{courseOverview.quantity}</p>
            </div>
            <div className="stats-card">
                <h3 className="icons-lesson-detail-completion"><GiTakeMyMoney /></h3>
                <p>{courseOverview.totalPrice !== null ? courseOverview.totalPrice.toLocaleString('de-DE') + ' VND' : '0'}</p>
            </div>
        </div>
    );
}

export default StatisticsOverview;

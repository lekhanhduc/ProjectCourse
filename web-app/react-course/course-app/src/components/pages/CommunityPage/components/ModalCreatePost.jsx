import { Button, Form } from "react-bootstrap";
import { FaImage, FaSmile, FaVideo } from "react-icons/fa";
import avatarDefault from '../../../../img/avatar-default.jpg'

const ModalCreatePost = (props) => {
    const {setShowModal, avatar} = props;
    return (
        <div className="create-post-container">
            <div className="create-post-header d-flex align-items-center">
                <img src={avatar || avatarDefault} alt="User Avatar" className="avatar-img me-3" />
                <Form.Control
                    type="text"
                    placeholder="Đức ơi, bạn đang nghĩ gì thế?"
                    onClick={() => setShowModal(true)}
                    className="create-post-input"
                />
            </div>
            <div className="create-post-footer d-flex justify-content-around mt-3">
                <Button variant="outline-danger" className="d-flex align-items-center">
                    <FaVideo className="me-1" />
                    Live Video
                </Button>
                <Button variant="outline-success" className="d-flex align-items-center">
                    <FaImage className="me-1" />
                    Photo/Video
                </Button>
                <Button variant="outline-warning" className="d-flex align-items-center">
                    <FaSmile className="me-1" />
                    Feeling/Activity
                </Button>
            </div>
        </div>
    );
}

export default ModalCreatePost;
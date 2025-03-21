import React from "react";

export const Search = ({ onSearch, searchParams, setTitle, setAuthor, setCourseLevel, setLanguage, setMinPoints, setMaxPoints }) => {

    const handleSearch = () => {
        let filterQuery = '';

        if (searchParams.title) filterQuery += `title~~'*${searchParams.title}*'`;
        if (searchParams.author) filterQuery += (filterQuery ? ' and ' : '') + `author.name~~'*${searchParams.author}*'`;
        if (searchParams.courseLevel) filterQuery += (filterQuery ? ' and ' : '') + `courseLevel~~'*${searchParams.courseLevel}*'`;
        if (searchParams.language) filterQuery += (filterQuery ? ' and ' : '') + `language~~'*${searchParams.language}*'`;
        if (searchParams.minPoints) filterQuery += (filterQuery ? ' and ' : '') + `points>:${searchParams.minPoints}`;
        if (searchParams.maxPoints) filterQuery += (filterQuery ? ' and ' : '') + `points<:${searchParams.maxPoints}`;

        if (filterQuery) {
            onSearch(filterQuery);
        } else {
            alert('Please enter at least one search criterion.');
        }
    };

    return (
        <div className="content-page">
            <div className="container-fluid mb-5">
                <div className="search-bar p-4 rounded shadow-sm custom-search-bar">
                    <div className="row justify-content-center align-items-center">
                        {/* Title Search */}
                        <div className="col-lg-2 col-md-4 col-sm-6 mb-3">
                            <input
                                type="text"
                                className="form-control search-input custom-input"
                                placeholder="Search by Title"
                                value={searchParams.title}
                                onChange={(e) => setTitle(e.target.value)}
                            />
                        </div>

                        {/* Author Search */}
                        <div className="col-lg-2 col-md-4 col-sm-6 mb-3">
                            <input
                                type="text"
                                className="form-control search-input custom-input"
                                placeholder="Search by Author"
                                value={searchParams.author}
                                onChange={(e) => setAuthor(e.target.value)}
                            />
                        </div>

                        {/* Course Level Search */}
                        <div className="col-lg-2 col-md-4 col-sm-6 mb-3">
                            <select
                                className="form-control search-input custom-input"
                                value={searchParams.courseLevel}
                                onChange={(e) => setCourseLevel(e.target.value)}
                            >
                                <option value="">All Levels</option>
                                <option value="BEGINNER">BEGINNER</option>
                                <option value="INTERMEDIATE">INTERMEDIATE</option>
                                <option value="ADVANCED">ADVANCED</option>
                                <option value="EXPERT">EXPERT</option>
                            </select>
                        </div>

                        {/* Language Search */}
                        <div className="col-lg-2 col-md-4 col-sm-6 mb-3">
                            <select
                                className="form-control search-input custom-input"
                                value={searchParams.language}
                                onChange={(e) => setLanguage(e.target.value)}
                            >
                                <option value="">All Languages</option>
                                <option value="English">English</option>
                                <option value="Spanish">Spanish</option>
                                <option value="French">French</option>
                                <option value="German">German</option>
                                <option value="India">INDIA</option>
                                <option value="VietNamese">VIETNAMESE</option>
                            </select>
                        </div>

                        {/* Min Points Search */}
                        <div className="col-lg-2 col-md-4 col-sm-6 mb-3">
                            <input
                                type="number"
                                className="form-control search-input custom-input"
                                placeholder="Min Points"
                                value={searchParams.price}
                                onChange={(e) => setMinPoints(e.target.value)}
                            />
                        </div>

                        {/* Max Points Search */}
                        <div className="col-lg-2 col-md-4 col-sm-6 mb-3">
                            <input
                                type="number"
                                className="form-control search-input custom-input"
                                placeholder="Max Points"
                                value={searchParams.price}
                                onChange={(e) => setMaxPoints(e.target.value)}
                            />
                        </div>

                        {/* Search Button */}
                        <div className="col-lg-2 col-md-4 col-sm-6 mb-3">
                            <button className="btn btn-primary btn-block custom-btn" onClick={handleSearch}>
                                Search
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

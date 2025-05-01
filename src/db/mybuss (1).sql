-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 01, 2025 at 04:51 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `mybuss`
--

-- --------------------------------------------------------

--
-- Table structure for table `tbl_bus`
--

CREATE TABLE `tbl_bus` (
  `b_id` int(11) NOT NULL,
  `b_name` varchar(255) NOT NULL,
  `b_model` varchar(255) NOT NULL,
  `b_capacity` varchar(255) NOT NULL,
  `b_status` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tbl_bus`
--

INSERT INTO `tbl_bus` (`b_id`, `b_name`, `b_model`, `b_capacity`, `b_status`) VALUES
(2, 'People Bus', 'Model A', '20', 'Active '),
(3, 'Ceris ', 'Model B', '30', 'Active '),
(4, 'Busses', 'Model C', '15', 'Active '),
(5, 'Meme', 'Model D', '22', 'Active '),
(6, 'hehe', 'Model b', '45', 'Active '),
(7, 'Lodge', 'suzuki', '26', 'Active ');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_log`
--

CREATE TABLE `tbl_log` (
  `log_id` int(11) NOT NULL,
  `u_id` int(11) NOT NULL,
  `u_username` varchar(50) NOT NULL,
  `login_time` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `u_type` varchar(50) NOT NULL,
  `log_status` enum('Pending','Active','Inactive','') NOT NULL,
  `logout_time` timestamp NULL DEFAULT NULL,
  `log_description` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tbl_log`
--

INSERT INTO `tbl_log` (`log_id`, `u_id`, `u_username`, `login_time`, `u_type`, `log_status`, `logout_time`, `log_description`) VALUES
(1, 2, 'mark123', '2025-05-01 01:58:26', 'Staff', 'Active', NULL, 'User Changed Their Details'),
(2, -1, 'mark123', '2025-05-01 02:38:39', 'Failed - Invalid Login', 'Active', NULL, NULL),
(3, -1, 'mark123', '2025-05-01 02:38:43', 'Failed - Invalid Login', 'Active', NULL, NULL),
(4, -1, 'ross123', '2025-05-01 02:49:54', 'Failed - Invalid Login', 'Inactive', '2025-05-01 02:49:54', NULL),
(5, 1, 'ross123', '2025-05-01 02:49:54', 'Success - Admin Login', 'Inactive', '2025-05-01 02:49:54', NULL),
(6, 3, 'hell123', '2025-05-01 05:46:50', 'Success - User Action', 'Inactive', '2025-05-01 05:46:50', 'New user registered: hell123'),
(7, 3, 'hell123', '2025-05-01 05:46:50', 'Failed - Inactive Account', 'Inactive', '2025-05-01 05:46:50', NULL),
(8, 3, 'hell123', '2025-05-01 05:46:50', 'Success - Staff Login', 'Inactive', '2025-05-01 05:46:50', NULL),
(9, 3, 'hell123', '2025-05-01 14:50:24', 'Success - Admin Login', 'Inactive', '2025-05-01 14:50:24', NULL),
(10, 3, 'hell123', '2025-05-01 14:50:24', 'Admin', 'Inactive', '2025-05-01 14:50:24', 'Admin Changed Their Profile'),
(11, 3, 'hell123', '2025-05-01 14:50:24', 'Success - Admin Login', 'Inactive', '2025-05-01 14:50:24', NULL),
(12, 3, 'hell123', '2025-05-01 14:50:24', 'Success - Admin Login', 'Inactive', '2025-05-01 14:50:24', NULL),
(13, 3, 'hell123', '2025-05-01 14:50:24', 'Success - Admin Login', 'Inactive', '2025-05-01 14:50:24', NULL),
(14, 3, 'hell123', '2025-05-01 14:50:24', 'Success - Admin Login', 'Inactive', '2025-05-01 14:50:24', NULL),
(15, 3, 'hell123', '2025-05-01 14:50:24', 'Success - Admin Login', 'Inactive', '2025-05-01 14:50:24', NULL),
(16, 3, 'hell123', '2025-05-01 14:50:24', 'Success - Admin Login', 'Inactive', '2025-05-01 14:50:24', NULL),
(17, 3, 'hell123', '2025-05-01 14:50:24', 'Success - Admin Login', 'Inactive', '2025-05-01 14:50:24', NULL),
(18, 3, 'hell123', '2025-05-01 14:50:24', 'Success - Admin Login', 'Inactive', '2025-05-01 14:50:24', NULL),
(19, 3, 'hell123', '2025-05-01 14:50:24', 'Success - Admin Login', 'Inactive', '2025-05-01 14:50:24', NULL),
(20, 3, 'hell123', '2025-05-01 14:50:24', 'Success - Admin Login', 'Inactive', '2025-05-01 14:50:24', NULL),
(21, 3, 'hell123', '2025-05-01 14:50:24', 'Success - Admin Login', 'Inactive', '2025-05-01 14:50:24', NULL),
(22, 3, 'hell123', '2025-05-01 14:50:24', 'Admin', 'Inactive', '2025-05-01 14:50:24', 'Admin Added a New Account: batman123'),
(23, 4, 'batman123', '2025-05-01 14:47:27', 'Success - Staff Login', 'Inactive', '2025-05-01 14:47:27', NULL),
(24, 3, 'hell123', '2025-05-01 14:50:24', 'Success - Admin Login', 'Inactive', '2025-05-01 14:50:24', NULL),
(25, 3, 'hell123', '2025-05-01 14:50:24', 'Success - Admin Login', 'Inactive', '2025-05-01 14:50:24', NULL),
(26, 3, 'hell123', '2025-05-01 12:32:51', 'Success - Admin Login', 'Inactive', '2025-05-01 12:32:51', NULL),
(27, 3, 'hell123', '2025-05-01 12:33:41', 'Success - Admin Login', 'Inactive', '2025-05-01 12:33:41', NULL),
(28, 3, 'hell123', '2025-05-01 12:35:47', 'Success - Admin Login', 'Inactive', '2025-05-01 12:35:47', NULL),
(29, 3, 'hell123', '2025-05-01 12:36:56', 'Success - Admin Login', 'Inactive', '2025-05-01 12:36:56', NULL),
(30, 3, 'hell123', '2025-05-01 14:46:56', 'Success - Admin Login', 'Inactive', '2025-05-01 14:46:56', NULL),
(31, 4, 'batman123', '2025-05-01 14:47:27', 'Success - Staff Login', 'Inactive', '2025-05-01 14:47:27', NULL),
(32, 4, 'batman123', '2025-05-01 14:47:27', 'Success - Staff Login', 'Inactive', '2025-05-01 14:47:27', NULL),
(33, 4, 'batman123', '2025-05-01 14:47:40', 'Success - Staff Login', 'Active', NULL, NULL),
(34, 3, 'hell123', '2025-05-01 14:50:24', 'Success - Admin Login', 'Inactive', '2025-05-01 14:50:24', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_routes`
--

CREATE TABLE `tbl_routes` (
  `r_id` int(11) NOT NULL,
  `r_from` varchar(100) NOT NULL,
  `r_destination` varchar(100) NOT NULL,
  `r_fare` decimal(10,2) NOT NULL,
  `r_status` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tbl_routes`
--

INSERT INTO `tbl_routes` (`r_id`, `r_from`, `r_destination`, `r_fare`, `r_status`) VALUES
(1, 'Minglanilla', 'SM seaside', 30.00, 'Active '),
(2, 'Hellside', 'Ayala', 45.00, 'Active '),
(3, 'Hellside', 'Ayala', 45.00, 'Active ');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_users`
--

CREATE TABLE `tbl_users` (
  `u_id` int(11) NOT NULL,
  `u_fname` varchar(55) NOT NULL,
  `u_lname` varchar(55) NOT NULL,
  `u_username` varchar(55) NOT NULL,
  `u_email` varchar(55) NOT NULL,
  `u_num` varchar(55) NOT NULL,
  `u_password` varchar(55) NOT NULL,
  `u_type` varchar(55) NOT NULL,
  `u_status` varchar(55) NOT NULL,
  `security_question` varchar(55) NOT NULL,
  `security_answer` varchar(55) NOT NULL,
  `u_image` varchar(500) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tbl_users`
--

INSERT INTO `tbl_users` (`u_id`, `u_fname`, `u_lname`, `u_username`, `u_email`, `u_num`, `u_password`, `u_type`, `u_status`, `security_question`, `security_answer`, `u_image`) VALUES
(1, 'ross', 'sabio', 'ross123', 'rosssabio@gmail.com', '2147483647', 'ky88G1YlfOhTmsJp16q0JVDaz4gY0HXwvfGZBWKq4+8=', 'Admin', 'Active', '', '', ''),
(2, 'mark', 'pacaldo', 'mark123', 'markpacaldo@gmail.com', '2147483647', 'ky88G1YlfOhTmsJp16q0JVDaz4gY0HXwvfGZBWKq4+8=', 'Staff', 'Active', '', '', 'src/images/icons8-admin-70.png'),
(3, 'kill', 'hell', 'hell123', 'hell@gmail.com', '92346781', 'ky88G1YlfOhTmsJp16q0JVDaz4gY0HXwvfGZBWKq4+8=', 'Admin', 'Active', 'What\'s the name of your first pet?', 'Fkd2iMDgBpnGz6RJejYS1+g8UyBitkslD+2JCBKO1Ug=', 'src/images/icons8-admin-70.png'),
(4, 'asdasd', 'asdasd', 'batman123', 'batman123@gmail.com', 'U92346782', 'ky88G1YlfOhTmsJp16q0JVDaz4gY0HXwvfGZBWKq4+8=', 'Staff', 'Active', 'What\'s your favorite Color?', 'em2FljNj0cI/QQqbAXCHQtdpTvt/uLRqJ3eGoKfpMJ0=', 'batman123_icons8-bus-70.png');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `tbl_bus`
--
ALTER TABLE `tbl_bus`
  ADD PRIMARY KEY (`b_id`);

--
-- Indexes for table `tbl_log`
--
ALTER TABLE `tbl_log`
  ADD PRIMARY KEY (`log_id`);

--
-- Indexes for table `tbl_routes`
--
ALTER TABLE `tbl_routes`
  ADD PRIMARY KEY (`r_id`);

--
-- Indexes for table `tbl_users`
--
ALTER TABLE `tbl_users`
  ADD PRIMARY KEY (`u_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `tbl_bus`
--
ALTER TABLE `tbl_bus`
  MODIFY `b_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `tbl_log`
--
ALTER TABLE `tbl_log`
  MODIFY `log_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=35;

--
-- AUTO_INCREMENT for table `tbl_routes`
--
ALTER TABLE `tbl_routes`
  MODIFY `r_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `tbl_users`
--
ALTER TABLE `tbl_users`
  MODIFY `u_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

-- 1. Accounts Table
CREATE TABLE `accounts` (
  `account_no` varchar(15) NOT NULL,
  `holder_name` varchar(50) NOT NULL,
  `balance` decimal(12,2) NOT NULL DEFAULT 0.00,
  `create_at` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`account_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2. Transactions Table
CREATE TABLE `transactions` (
  `id` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `account_no` varchar(15) NOT NULL,
  `transaction_type` varchar(50) NOT NULL,
  `amount` decimal(12,2) NOT NULL,
  `description` varchar(255) NOT NULL,
  `transaction_date` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_account_transactions` FOREIGN KEY (`account_no`) REFERENCES `accounts` (`account_no`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3. Insert Accounts Data (Based on reference image)
INSERT INTO `accounts` (`account_no`, `holder_name`, `balance`, `create_at`) VALUES
('2026101001', 'Daven Reyes Umerez', 34000.00, '2026-10-15 21:28:20'),[cite: 1]
('2026101002', 'David Reyes Umerez', 35000.00, '2026-10-18 12:25:01'),[cite: 1]
('2026101003', 'Rose Reyes Umerez', 40000.00, '2026-10-18 12:26:01'),[cite: 1]
('2026101004', 'Olive Reyes Umercz', 50000.00, '2026-10-18 12:26:36'),[cite: 1]
('2026101005', 'Melanie Reyes Umerez', 100000.00, '2026-10-18 12:27:05');[cite: 1]

-- 4. Insert Corresponding Transactions Data
INSERT INTO `transactions` (`id`, `account_no`, `transaction_type`, `amount`, `description`, `transaction_date`) VALUES
('TXN10001', '2026101001', 'DEPOSIT', 34000.00, 'Initial deposit for Daven', '2026-10-15 21:30:00'),
('TXN10002', '2026101002', 'DEPOSIT', 35000.00, 'Initial deposit for David', '2026-10-18 12:28:00'),
('TXN10003', '2026101003', 'DEPOSIT', 40000.00, 'Initial deposit for Rose', '2026-10-18 12:29:00'),
('TXN10004', '2026101004', 'DEPOSIT', 50000.00, 'Initial deposit for Olive', '2026-10-18 12:30:00'),
('TXN10005', '2026101005', 'DEPOSIT', 100000.00, 'Initial deposit for Melanie', '2026-10-18 12:31:00');
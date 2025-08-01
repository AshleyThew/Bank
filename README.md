# Bank Plugin

[![Version](https://img.shields.io/badge/version-5.0.0--RELEASE-blue)](https://github.com/AshleyThew/Bank)
[![Minecraft](https://img.shields.io/badge/minecraft-1.13+-green)](https://www.spigotmc.org/resources/bank-updated.3556)
[![Java](https://img.shields.io/badge/java-8+-orange)](https://www.oracle.com/java/)

A comprehensive and feature-rich bank plugin for Minecraft servers that allows players to store money, experience, and items safely. Perfect for economy servers and provides advanced features like loans, interest, and administrative tools.

## 📋 Features

### 💰 Money Management
- **Bank Account System**: Secure money storage separate from player inventory economy
- **Interest System**: Configurable online/offline interest rates with customizable minimums and maximums
- **Transaction History**: Track all money deposits, withdrawals, and transfers with detailed logs
- **Tax System**: Configurable deposit/withdrawal taxes with server-wide tax storage
- **Money Limits**: Set maximum money limits per player with permission-based overrides
- **Cheque System**: Create transferable cheques with cheque books for easy money transfers

### 📊 Experience Banking
- **EXP Storage**: Store and withdraw experience points safely
- **EXP Interest**: Earn interest on banked experience over time
- **EXP Limits**: Configure maximum experience storage limits
- **Experience History**: Complete transaction tracking for all EXP operations
- **Tax on EXP**: Optional taxation on experience deposits and withdrawals

### 🎒 Item Banking
- **Unlimited Storage**: Store items in expandable bank tabs
- **Tab System**: Organize items across multiple tabs (up to 9 tabs by default)
- **Tab Permissions**: Permission-based access to different tab amounts
- **Buyable Tabs**: Players can purchase additional tabs with money
- **Buyable Slots**: Expand storage capacity by purchasing more slots
- **Item Blacklist/Whitelist**: Control which items can be stored
- **Trash Can**: Built-in item disposal with blacklist protection
- **Hotbar Swapping**: Quick item management between inventory and bank
- **Tab Renaming**: Custom names for organization
- **Locked Slots**: Premium slots that can be unlocked via purchase

### 🏦 Advanced Features
- **Loan System**: 
  - Take out loans with configurable interest rates
  - Automatic payback systems with custom rates
  - Loan deadlines with command execution on failure
  - Interest accumulation over time
  - Payback history and management
- **Banking Types**:
  - Block-based banks (interact with placed blocks)
  - Citizens NPC integration
  - Command-based access
- **PIN Security**: Optional PIN protection for bank access
- **Death Penalties**: Configurable loss of items/money/exp on death

### 🛠 Administrative Tools
- **Admin Commands**: Complete administrative control over all player accounts
- **Force Bank Access**: Admins can access any player's bank
- **Account Management**: Add/subtract money, exp, manage slots and tabs
- **Tax Management**: View and withdraw collected taxes
- **Debug Tools**: Extensive debugging and logging systems
- **Player Locking**: Temporarily lock player accounts

### 🔧 Technical Features
- **Multiple Database Support**:
  - SQLite (default, no setup required)
  - MySQL (for multi-server setups)
  - MongoDB (with MongoDB loader plugin)
- **Multi-Server Sync**: Full support for synchronized data across multiple servers
- **Auto-Save**: Configurable automatic saving with custom intervals
- **Data Conversion**: Built-in converters for other bank plugins (BankCraft, ChestBank, EconomyBank, etc.)
- **Placeholder Support**: PlaceholderAPI and MVdWPlaceholderAPI integration
- **Vault Integration**: Full Vault economy override support
- **Skript Support**: Integration with Skript for custom scripts

### 🎮 User Interface
- **GUI-Based**: Complete graphical user interface for all operations
- **Sound Effects**: Configurable sound system for user feedback
- **Custom Inventories**: Professionally designed interfaces for all features
- **Anvil Input**: Secure input system for amounts and values
- **Multi-Language**: Configurable language system
- **Format Options**: Customizable money and number formatting

## 📦 Installation

### Requirements
- **Minecraft Server**: Spigot/Paper 1.13+ (recommended: latest version)
- **Java**: Java 8 or higher
- **Core Plugin**: Will be automatically downloaded on first run

### Basic Setup

1. **Download the Plugin**
   - Download `bank-plugin-latest-all.jar` from releases
   - Place in your server's `plugins` folder

2. **First Run**
   - Start your server
   - The plugin will automatically download required dependencies
   - Stop the server after initial generation

3. **Basic Configuration**
   - Edit `plugins/Bank/config.yml` for basic settings
   - Configure your preferred database type (SQLite is default)
   - Restart the server

### Database Configuration

#### SQLite (Default - No Setup Required)
```yaml
BANK:
  SAVE_TYPE: SQLITE
```

#### MySQL Setup
1. **Configure MySQL**:
   ```yaml
   BANK:
     SAVE_TYPE: MYSQL
   ```

2. **Edit MySQL Configuration**:
   ```yaml
   # plugins/Bank/mysql.yml
   mysql:
     host: localhost
     port: 3306
     database: bank
     username: your_username
     password: your_password
   ```

#### MongoDB Setup
1. **Install MongoDB Loader Plugin**:
   - Download from: https://dev.bukkit.org/projects/mongodb-loader
   - Place in plugins folder

2. **Configure MongoDB**:
   ```yaml
   BANK:
     SAVE_TYPE: MONGODB
   ```

3. **Edit MongoDB Configuration**:
   ```yaml
   # plugins/Bank/mongo.yml
   mongodb:
     host: localhost
     port: 27017
     database: bank
     username: your_username
     password: your_password
   ```

## ⚙️ Configuration

### Core Settings

```yaml
BANK:
  # Database type: SQLITE, MYSQL, MONGODB
  SAVE_TYPE: SQLITE
  
  # Auto-save settings
  SAVE_AUTO_ENABLED: true
  SAVE_AUTO_TIMER: 600  # seconds
  
  # Enable different bank types
  MONEY_ENABLED: true
  EXP_ENABLED: true
  ITEMS_ENABLED: true
  
  # Bank access types
  TYPE_BLOCK_ENABLED: true
  TYPE_CITIZENS_ENABLED: false
```

### Money Configuration

```yaml
BANK:
  # Money limits and defaults
  MONEY_MAX: 999999999.99
  MONEY_DEFAULT_ENABLED: false
  MONEY_DEFAULT_AMOUNT: 0
  
  # Interest system
  MONEY_INTEREST_ENABLED: false
  MONEY_INTEREST_ONLINE_PERCENT_GAINED: 0.01    # 1%
  MONEY_INTEREST_OFFLINE_PERCENT_GAINED: 0.001  # 0.1%
  MONEY_INTEREST_TIMER_TIME: 1800               # 30 minutes
  
  # Taxation
  MONEY_DEPOSIT_TAX_PERCENT: 0    # No tax
  MONEY_WITHDRAW_TAX_PERCENT: 0   # No tax
```

### Item Banking Configuration

```yaml
BANK:
  # Tab system
  ITEMS_TABS_ENABLED: true
  ITEMS_TABS_DEFAULT: 9
  ITEMS_TABS_BUY_ENABLED: false
  ITEMS_TABS_BUY_COST: 50
  
  # Slot system
  ITEMS_SLOTS_DEFAULT: 50
  ITEMS_SLOTS_BUY_ENABLED: false
  ITEMS_SLOTS_BUY_COST: 50
  
  # Features
  ITEMS_TRASHCAN_ENABLED: true
  ITEMS_BLACKLIST_ENABLED: false
  ITEMS_HOTBAR_SWAP_ENABLED: true
```

### Loan System Configuration

```yaml
BANK:
  # Loan settings
  LOANS_ENABLED: false
  LOANS_AMOUNT_DEFAULT: 100
  LOANS_AMOUNT_MINIMUM: 0
  LOANS_AMOUNT_MAXIMUM: 999999999
  
  # Deadlines
  LOANS_DEADLINE_ENABLED: false
  LOANS_DEADLINE_DAYS: 30
  LOANS_DEADLINE_COMMANDS:
    - "tempban <uuid> 10m Failed to pay $<amount> loan"
  
  # Interest and payback
  LOANS_INTEREST_ENABLED: false
  LOANS_PAYBACK_ENABLED: false
```

## 🎯 Commands

### Player Commands

| Command | Permission | Description |
|---------|------------|-------------|
| `/bank` | `bank.open` | Open main bank menu |
| `/bank open` | `bank.open` | Open main bank menu |
| `/bank money` | `bank.money` | Access money banking |
| `/bank money balance` | `bank.money.balance` | Check money balance |
| `/bank money deposit <amount>` | `bank.money.deposit` | Deposit money |
| `/bank money withdraw <amount>` | `bank.money.withdraw` | Withdraw money |
| `/bank money pay <player> <amount>` | `bank.money.pay` | Send money to player |
| `/bank exp` | `bank.exp` | Access experience banking |
| `/bank exp balance` | `bank.exp.balance` | Check EXP balance |
| `/bank exp deposit <amount>` | `bank.exp.deposit` | Deposit experience |
| `/bank exp withdraw <amount>` | `bank.exp.withdraw` | Withdraw experience |
| `/bank cheque create <amount> [player]` | `bank.cheque.create` | Create a cheque |
| `/bank cheque book` | `bank.cheque.book` | Get a cheque book |
| `/bank top money [page]` | `bank.top.money` | View money leaderboard |
| `/bank top exp [page]` | `bank.top.exp` | View EXP leaderboard |
| `/bank info` | `bank.info` | View bank information |

### Admin Commands

| Command | Permission | Description |
|---------|------------|-------------|
| `/bank admin reload` | `bank.admin.reload` | Reload plugin configuration |
| `/bank admin money add <player> <amount>` | `bank.admin.money.add` | Add money to player |
| `/bank admin money subtract <player> <amount>` | `bank.admin.money.subtract` | Remove money from player |
| `/bank admin money balance <player>` | `bank.admin.money.balance` | Check player's money |
| `/bank admin exp add <player> <amount>` | `bank.admin.exp.add` | Add EXP to player |
| `/bank admin exp subtract <player> <amount>` | `bank.admin.exp.subtract` | Remove EXP from player |
| `/bank admin open <player>` | `bank.admin.open` | Open player's bank |
| `/bank admin force open <player>` | `bank.admin.force.open` | Force open bank |
| `/bank admin lock <player> <true/false>` | `bank.admin.lock` | Lock/unlock player account |
| `/bank admin save` | `bank.admin.save` | Force save all data |
| `/bank admin slots add <player> <amount>` | `bank.admin.slots.add` | Add slots to player |
| `/bank admin slots set <player> <amount>` | `bank.admin.slots.set` | Set player's slots |
| `/bank admin tax info` | `bank.admin.tax.info` | View tax information |
| `/bank admin tax withdraw <amount>` | `bank.admin.tax.withdraw` | Withdraw collected taxes |
| `/bank admin block add` | `bank.admin.block.add` | Add bank block |
| `/bank admin block remove` | `bank.admin.block.remove` | Remove bank block |

## 🔐 Permissions

### Basic Permissions

| Permission | Description | Default |
|------------|-------------|---------|
| `bank.open` | Access to bank | `true` |
| `bank.money` | Access money banking | `true` |
| `bank.money.deposit` | Deposit money | `true` |
| `bank.money.withdraw` | Withdraw money | `true` |
| `bank.money.balance` | Check money balance | `true` |
| `bank.money.pay` | Send money to players | `true` |
| `bank.exp` | Access experience banking | `true` |
| `bank.exp.deposit` | Deposit experience | `true` |
| `bank.exp.withdraw` | Withdraw experience | `true` |
| `bank.exp.balance` | Check EXP balance | `true` |
| `bank.cheque` | Access cheque system | `true` |
| `bank.cheque.create` | Create cheques | `true` |
| `bank.cheque.book` | Get cheque books | `true` |

### Advanced Permissions

| Permission | Description | Default |
|------------|-------------|---------|
| `bank.tabs.<number>` | Access to specific tab amount | `false` |
| `bank.slots.<number>` | Access to specific slot amount | `false` |
| `bank.money.amount.<amount>` | Maximum money limit | `false` |
| `bank.exp.amount.<amount>` | Maximum EXP limit | `false` |
| `bank.loans.amount.<amount>` | Maximum loan amount | `false` |
| `bank.money.interest.<percentage>` | Custom interest rate | `false` |
| `bank.exp.interest.<percentage>` | Custom EXP interest rate | `false` |

### Admin Permissions

| Permission | Description | Default |
|------------|-------------|---------|
| `bank.admin` | Basic admin access | `op` |
| `bank.admin.*` | All admin permissions | `op` |
| `bank.admin.reload` | Reload configuration | `op` |
| `bank.admin.money.*` | All money admin commands | `op` |
| `bank.admin.exp.*` | All EXP admin commands | `op` |
| `bank.admin.open` | Open player banks | `op` |
| `bank.admin.force.*` | Force actions | `op` |
| `bank.admin.lock` | Lock player accounts | `op` |

## 🔌 Plugin Integration

### Vault Integration
- Full economy provider support
- Override existing economy plugins
- Custom economy commands with `/bank eco`

### PlaceholderAPI Support
Available placeholders:
- `%bank_money%` - Player's banked money
- `%bank_exp%` - Player's banked experience  
- `%bank_money_formatted%` - Formatted money amount
- `%bank_exp_formatted%` - Formatted EXP amount
- `%bank_loans_total%` - Total loan amount
- `%bank_loans_count%` - Number of active loans

### Citizens NPC Integration
- Create bank NPCs using `/trait add bank-trait`
- Players can interact with NPCs to access banks
- Configurable NPC banking restrictions

### Skript Integration
- Full API support for custom scripts
- Access all bank functions through Skript
- Custom events for bank transactions

## 🔄 Data Migration

### From Other Bank Plugins

The plugin includes built-in converters for:
- **BankCraft**: Complete data migration
- **ChestBank**: Full account transfer
- **EconomyBank**: Money and data conversion
- **TimeIsMoney**: Account migration
- **BankPlus**: Data transfer

### Migration Process
1. **Backup your data** before migration
2. Keep the old plugin's data folder
3. Remove the old plugin JAR file
4. Enable the appropriate converter in config:
   ```yaml
   BANK:
     CONVERTER_BANKCRAFT_ENABLED: true
     CONVERTER_CHESTBANK_ENABLED: true
     CONVERTER_ECONOMYBANK_ENABLED: true
   ```
5. Restart the server
6. Verify data migration completed successfully

## 🐛 Troubleshooting

### Common Issues

**Plugin not loading:**
- Ensure you're running Spigot/Paper 1.13+
- Check that Java 8+ is installed
- Verify the core plugin was downloaded correctly

**Database connection errors:**
- Check MySQL/MongoDB credentials in config files
- Ensure database server is running and accessible
- Verify firewall settings allow connections

**Permission issues:**
- Check your permissions plugin configuration
- Verify player has required permissions
- Use `/bank admin debug` for permission testing

**Data not saving:**
- Check auto-save settings in config
- Verify database connection is stable
- Check server logs for save errors

### Performance Optimization

**For large servers:**
- Use MySQL instead of SQLite
- Increase auto-save timer
- Enable specific save triggers only when needed
- Configure appropriate interest timer intervals

**Memory optimization:**
- Set reasonable transaction history limits
- Use slot limits to prevent excessive item storage
- Configure tab limits based on server capacity

## 📊 API Usage

### For Developers

```java
// Get Bank API instance
BankAPI api = BankAPI.getInstance();

// Player money operations
double money = api.getMoney(uuid);
api.setMoney(uuid, amount);
api.addMoney(uuid, amount);
api.subtractMoney(uuid, amount);

// Player experience operations  
double exp = api.getExp(uuid);
api.setExp(uuid, amount);
api.addExp(uuid, amount);
api.subtractExp(uuid, amount);

// Open bank for player
api.openBank(player);
api.openBank(player, OpenTypes.MONEY); // Specific section
```

## 📝 Support

- **Documentation**: [GitHub Wiki](https://github.com/AshleyThew/Bank/wiki)
- **Issues**: [GitHub Issues](https://github.com/AshleyThew/Bank/issues)
- **Discord**: Join our community server for support
- **SpigotMC**: [Plugin Page](https://www.spigotmc.org/resources/bank-updated.3556)

## 📄 License

This project is licensed under a custom license. Please see the [LICENSE](LICENSE) file for details.

## 🏆 Credits

- **Author**: Dablakbandit (Ashley Thew)
- **Contributors**: See [GitHub Contributors](https://github.com/AshleyThew/Bank/graphs/contributors)
- **Special Thanks**: To the Minecraft community for feedback and testing

---

*Bank Plugin - The most comprehensive banking solution for Minecraft servers*

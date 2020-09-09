There are 8 fragments and one activity

8 fragments are:

1. Add Transaction Fragment
2. Home Screen Fragment
3. Select Budget Fragment
4. Profile Fragment
5. PieChart Fragment
6. Transactions View All Fragment
7. Transactions View All Expanded
8. Upcoming Transactions View all

Select Budget Fragment - It's an onboarding screen to select budget, income and profile name. Profile name is compulsory, to achieve that I've used textWatcher and until you type your
profile name you can't proceed and continue button will be disabled.

Home Screen Fragment - On top app welcomes user ("Hi user"), and just below that it shows the total expends in particular month along with the name of month. Below that there is a progress
bar to notify how much percent of transactions are spends and how much percent of money left according to your budget.
There are two list on the homescreen-
-> Transactions - Shows all the transactions which are done before or on the current date. If no transactions are present then the image notifying add your transactions here will be visible.
This list shows max of 5 latest transactions (according to date). To view all the transactions click on "View All". I've implemented swipe functionality in this list, if user swipe left, 
transaction will get deleted, and snackbar will pop with undo action

-> Upcoming Transactions - Show all the upcoming transactions which user will be going to after the current date. If no upcoming transactions are present then the image notifying add your
upcoming transactions here will be visible. This list shows max of 5 latest upcoming transactions (according to date). To view all the upcoming transactions click on "View All". I've 
implemented swipe functionality in this list, if user swipe left, upcoming transaction will get deleted, and snackbar will pop with undo action. If user swipe right, user can add that upcoming 
transaction to transaction list.

Then there is a floating button signifies add transaction on the home screen. On the top right corner there are 2 icons-
-> pie chart icon - opens pie chart fragment. 
-> user profile icon - opens profile fragment.

Add Transaction Fragment - when floating button is clicked, it means user wants to add a new transaction and the header of the page will be "Add Transaction" and it will show some fields that
will fill for the transactions. These fields are - Transaction name, amount, select date, category, source, and comments.
-> Transaction name - It is a compulsory field, in this user have to enter the name of the transaction.
-> Amount - It is a compulsory field, in this user have to enter the amount of the transaction.
-> Select Date - Implemented using date picker, it is also a compulsory field. By default it is already selected on current date, user just have to click that field and the date picker will pop
   up and press ok to select current date or another date. There is a twist in this field, if user select current date or date before that then the transaction will be shown in Transactions
   list and if user select any future date, the transaction will be saved in Upcoming Transaction List.
-> Category - I've selected 12 predefined categories - Bill, EMI, Entertainment, Food, Fuel, Groceries, Health, Investment, Other, Shopping, Transfer, Travel. By default other is selected.
   This field is a drop down with autotextview. And I've made Leading icons according to all these categories.
-> Source - It is also drop down with autotextview with 3 choices- Cash, Debit Card and Credit Card.
-> Comments - Any other detail related to the transaction can be written here.
Finally there are 2 buttons to save transaction as income or expense.
There's one extra small detail i've paid attention to, if user selects a transaction from the details, it will open the same add transaction fragment but with header as "Transaction details".
And all the details of that transactions until user press the pencil icon on the top and user can also delete the transaction from here as well and after pressing the delete button on the top,
it will ask a confirmation message before deleting.

Pie chart fragment - It shows the monthly budget and savings pie chart, to view the well differentiated pie chart user first has to enter the budget and income in profile page.

Profile Fragment - It shows the profile of the user with 3 fields - Name, monthly budget and monthly income, and icon if user wants to change any detail he has to click on pencil button.

Transactions View all Fragment - After pressing view all on transaction list this fragment come into picture. Here all the transactions are stored in clubbed form and each transaction is clubbed
according to its month and year and it also shows if user was in the budget limit or exceeded according to budget for the particular month of a year.

Transactions View all expanded - After clicking any of the clubbed transactions in the Transactions view all fragment, this shows all the transactions happened in particular month of a year.

Upcoming View all - Shows all the upcoming transactions.








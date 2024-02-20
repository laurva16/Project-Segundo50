package co.edu.uptc.run;

public class AppRun2 {
    public static void main(String[] args) {

        AppMenus2 menus2 = new AppMenus2();
        int op = 0;
        while (op != -1) {
            switch (op) {
                case 0:
                    op = menus2.principalMenu(op);
                    break;
                case 1:
                    op = menus2.logInMenu(op);
                    break;
                case 2:
                    op = menus2.registerMenu(op);
                    break;
                case 3:
                    op = menus2.awayMenu(op);
                    break;
                case 4:
                    op = menus2.administratorMenu(op);
                    break;
                case 5:
                    op = menus2.moviesManagementMenu(op);
                    break;
                case 6:
                    op = menus2.ShowMoviesMenu(op);
                    break;
                case 7:
                    op = menus2.createMoviesMenu(op);
                    break;
                case 8:
                    op = menus2.updateMoviesMenu(op);
                    break;
                case 9:
                    op = menus2.deleteMovieMenu(op);
                    break;
                case 10:
                    op = menus2.SeriesManagementMenu(op);
                    break;
                case 11:
                    op = menus2.createSerieMenu(op);
                    break;
                case 12:
                    op = menus2.ShowSeriesMenu(op);
                    break;
                case 13:
                    op = menus2.updateSeriesMenu(op);
                    break;
                case 14:
                    op = menus2.deleteSeriesMenu(op);
                    break;
                case 20:
                    op = menus2.userRegisteredMenu(op);
                    break;
                case 21:
                    op = menus2.ShowMovies(op);
                    break;
                case 22:
                    op = menus2.ShowSeries(op);
                    break;
                case 25:
                    op = menus2.subscriptionManagerMenu(op);
                    break;
                case 26:
                    op = menus2.seeSubscriptionsMenu();
                    break;
                case 27:
                    op = menus2.createSubscriptionsMenu(op);
                    break;
                case 28:
                    op = menus2.updatesubscriptionMenu(op);
                    break;
                case 29:
                    op = menus2.removeSubscriptionMenu(op);
                    break;
                case 30:
                    op = menus2.playListMenu(op);
                    break;
                case 31:
                    op = menus2.seePlayListMenu(op);
                    break;
                case 32:
                    op = menus2.createPlayListMenu(op);
                    break;
                case 33:
                    op = menus2.updatePlayListMenu(op);
                    break;
                case 34:
                    op = menus2.removePlayListMenu(op);
                    break;
                case 35:
                    op = menus2.categoryMenu(op);
                    break;
                case 36:
                    op = menus2.seeCategorytMenu(op);
                    break;
                case 37:
                    op = menus2.createCategoryMenu(op);
                    break;
                case 38:
                    op = menus2.updateCategoryMenu(op);
                    break;
                case 39:
                    op = menus2.removeCategoryMenu(op);
                    break;
                case 40:
                    op = menus2.userSubMenu(op);
                    break;
                case 41:
                    op = menus2.userCategorytMenu(op);
                    break;
            }
        }

    }
}

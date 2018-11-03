package com.batook.test;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.Component;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class MyFrame extends JFrame {
    public static void main(String[] args) {
        JFrame.setDefaultLookAndFeelDecorated(true);
        SwingUtilities.invokeLater(() -> createGUI());
    }

    public static void createGUI() {
        MyFrame frame = new MyFrame();
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> System.exit(0));
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());
        frame.getContentPane()
             .add(closeButton);
        frame.pack();
        frame.setVisible(true);
    }
}


class CheckBoxTreeTest extends JFrame {
    private static final long serialVersionUID = 1L;
    final String ROOT = "Корневая запись";
    // Массив листьев деревьев
    final String[] nodes = new String[]{"Напитки", "Сладости"};
    final String[][] leafs = new String[][]{{"Чай", "Кофе", "Коктейль", "Сок", "Морс", "Минералка"}, {"Пирожное", "Мороженое", "Зефир", "Халва"}};

    public CheckBoxTreeTest() {
        super("Пример JTree с флажками");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        // Создание модели дерева
        TreeModel model = createTreeModel();
        // Создание дерева
        CheckBoxTree tree = new CheckBoxTree(model);
        // Размещение дерева в интерфейсе
        getContentPane().add(new JScrollPane(tree));
        // Вывод окна на экран
        setSize(400, 300);
        setVisible(true);
    }

    public static void main(String[] args) {
        new CheckBoxTreeTest();
    }

    // Иерархическая модель данных TreeModel для деревьев
    private TreeModel createTreeModel() {
        // Корневой узел дерева
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(ROOT);
        // Добавление ветвей - потомков 1-го уровня
        DefaultMutableTreeNode drink = new DefaultMutableTreeNode(nodes[0]);
        DefaultMutableTreeNode sweet = new DefaultMutableTreeNode(nodes[1]);
        // Добавление ветвей к корневой записи
        root.add(drink);
        root.add(sweet);
        // Добавление листьев - потомков 2-го уровня
        for (int i = 0; i < leafs[0].length; i++)
            drink.add(new DefaultMutableTreeNode(new CheckBoxElement(false, leafs[0][i])));
        for (int i = 0; i < leafs[1].length; i++)
            sweet.add(new DefaultMutableTreeNode(new CheckBoxElement(false, leafs[1][i])));
        // Создание стандартной модели
        return new DefaultTreeModel(root);
    }
}

class CheckBoxElement {
    // Данные узла
    public boolean selected;
    public String text;

    // Конструктор
    public CheckBoxElement(boolean selected, String text) {
        this.selected = selected;
        this.text = text;
    }
}


class CheckBoxTree extends JTree {
    private static final long serialVersionUID = 1L;
    // Стандартный объект для отображения узлов
    private DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();

    // Конструктор
    public CheckBoxTree(TreeModel model) {
        super(model);
        // Слушатель мыши
        addMouseListener(new MouseListener());
        // Определение собственного отображающего объекта
        setCellRenderer(new CheckBoxRenderer());
    }

    // Объект отображения узлов дерева с использованием флажков
    class CheckBoxRenderer extends JCheckBox implements TreeCellRenderer {
        private static final long serialVersionUID = 1L;

        public CheckBoxRenderer() {
            // Флажок будет прозрачным
            setOpaque(false);
        }

        // Метод получения компонента узла
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            // Проверка принадлежности узла к стандартной модели
            if (!(value instanceof DefaultMutableTreeNode)) {
                // Если нет, то используется стандартный объект
                return renderer.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
            }
            Object data = ((DefaultMutableTreeNode) value).getUserObject();
            // Проверка, являются ли данные CheckBoxElement
            if (data instanceof CheckBoxElement) {
                CheckBoxElement element = (CheckBoxElement) data;
                // Настройка флажка и текста
                setSelected(element.selected);
                setText(element.text);
                return this;
            }
            // В противном случае метод возвращает стандартный объект
            return renderer.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
        }
    }

    // Слушатель событий мыши
    class MouseListener extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            // Путь к узлу
            TreePath path = getClosestPathForLocation(e.getX(), e.getY());
            if (path == null) return;
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
            // Проверка принадлежности узла к стандартной модели
            if (node.getUserObject() instanceof CheckBoxElement) {
                // Изменение состояния флажка
                CheckBoxElement element = (CheckBoxElement) node.getUserObject();
                element.selected = !element.selected;
                repaint();
            }
        }
    }

}
